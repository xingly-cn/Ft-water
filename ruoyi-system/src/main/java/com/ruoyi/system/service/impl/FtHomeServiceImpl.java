package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.FtHome;
import com.ruoyi.system.domain.FtMessage;
import com.ruoyi.system.domain.FtNotices;
import com.ruoyi.system.domain.UserHome;
import com.ruoyi.system.exception.ServiceException;
import com.ruoyi.system.mapper.FtHomeMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.mapper.UserHomeMapper;
import com.ruoyi.system.request.HomeRequest;
import com.ruoyi.system.response.HomeResponse;
import com.ruoyi.system.service.FtHomeService;
import com.ruoyi.system.utils.DateUtils;
import com.ruoyi.system.utils.WechatUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/18 21:41
 */
@Service
@Slf4j
public class FtHomeServiceImpl implements FtHomeService {

    @Autowired
    private FtHomeMapper homeMapper;

    @Autowired
    private SysUserServiceImpl userService;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private UserHomeMapper userHomeMapper;

    @Autowired
    private FtMessageServiceImpl messageService;

    @Autowired
    private FtNoticesServiceImpl noticesService;

    @Override
    @CacheEvict(value = "home", allEntries = true)
    public Boolean deleteByPrimaryKey(Long id) {
        return homeMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    @CacheEvict(value = "home", allEntries = true)
    public Boolean addHome(FtHome record) {
        return homeMapper.insertSelective(record) > 0;
    }

    @Override
    @CacheEvict(value = "home", allEntries = true)
    public Boolean updateHome(FtHome record) {
        return homeMapper.updateByPrimaryKeySelective(record) > 0;
    }

    @Override
    public List<HomeResponse> homeTree() {
        List<HomeResponse> responses = Lists.newArrayList();

        List<FtHome> homes = getHomes();
        if (CollectionUtils.isEmpty(homes)) {
            return responses;
        }

        //找到所有的用户
        Map<Long, List<SysUser>> userMap = getUserMap();

        //先找到所有的一级菜单
        List<FtHome> parentHomes = homes.stream().filter(h -> h.getParentId().equals(0L)).collect(Collectors.toList());
        for (FtHome parentHome : parentHomes) {
            assembleHomeResponse(homes, userMap, responses, parentHome);
        }
        return responses;
    }

    @Override
    public Boolean addNumber(HomeRequest request) {
        HomeResponse home = selectByPrimaryKey(request.getId());
        if (home == null) {
            throw new SecurityException("该宿舍楼不存在");
        }

        //添加消息 确认之后在入库
        Long userId = SecurityUtils.getUserId();
        return sendMessageAndNotices(request.getId(), userId, true, home.getNumber(), request.getNumber(), false, 1);
    }

    @Override
    @Cacheable(value = "home", key = "#id")
    public HomeResponse selectByPrimaryKey(Long id) {
        FtHome home = homeMapper.selectByPrimaryKey(id);
        if (home != null) {
            HomeResponse response = new HomeResponse();
            BeanUtils.copyProperties(home, response);
            return response;
        }
        return new HomeResponse();
    }

    @Override
    public Boolean addUser(HomeRequest request) {
        HomeResponse home = selectByPrimaryKey(request.getId());
        if (home == null) {
            throw new SecurityException("该宿舍楼不存在");
        }
        //是否存在
        SysUser user = userService.selectUserById(request.getUserId());
        if (user == null) {
            throw new SecurityException("该用户不存在");
        }

        //查询该宿舍楼下面的管理员 最多俩个
        List<UserHome> userHomes = userHomeMapper.selectByHomeId(request.getId());
        if (userHomes.size() >= 2) {
            throw new SecurityException("该宿舍楼下面已经有两个管理员了，不能再添加了");
        }

        //查询该用户是否已经是管理员
        userHomes.stream().filter(u -> u.getUserId().equals(request.getUserId())).findAny().ifPresent(u -> {
            throw new SecurityException("该用户已经是该宿舍楼管理员了");
        });

        //添加管理员
        user.setRoleIds(new Long[]{7L});
        UserHome userHome = UserHome.builder()
                .homeId(request.getId())
                .userId(request.getUserId())
                .build();
        userHomeMapper.insert(userHome);
        return userService.updateUser(user) > 0;
    }

    @Override
    public Boolean addNumberByHomeId(Long homeId, Integer number) {
        FtHome home = homeMapper.selectByPrimaryKey(homeId);
        if (home == null) {
            throw new SecurityException("该宿舍楼不存在");
        }
        home.setNumber(home.getNumber() + number);
        return homeMapper.updateByPrimaryKeySelective(home) > 0;
    }

    @Override
    public List<HomeResponse> homeList(HomeRequest request) {
        List<FtHome> homes = homeMapper.selectList(request);
        if (CollectionUtils.isEmpty(homes)) {
            return Lists.newArrayList();
        }
        List<HomeResponse> responses = Lists.newArrayList();

        Map<Long, List<SysUser>> userMap = getUserMap();
        for (FtHome home : homes) {
            HomeResponse response = new HomeResponse();
            BeanUtils.copyProperties(home, response);
            response.setUsers(userMap.get(home.getId()) != null ? userMap.get(home.getId()) : Lists.newArrayList());
            responses.add(response);
        }
        return responses;
    }

    @Override
    public Map<String, Integer> count(Long homeId, Long userId) {
        List<UserHome> userHomes = userHomeMapper.selectByHomeId(homeId);

        if (CollectionUtils.isEmpty(userHomes)) {
            throw new ServiceException("该宿舍楼下面没有管理员");
        }

        userHomes.stream().filter(u -> u.getUserId().equals(userId)).findAny()
                .orElseThrow(() -> new ServiceException("该用户不是该宿舍楼的管理员"));


        //待入库的水的数量 栋楼的水的数量
        int waterCount = homeMapper.waterCount(homeId);
        int waterWaiteCount = messageService.waterWaiteCount(homeId, userId);
        return new HashMap<String, Integer>() {
            {
                put("waterCount", waterCount);
                put("waterWaiteCount", waterWaiteCount);
            }
        };
    }

    public String getSchoolByRemark(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return homeMapper.getSchoolByRemark(name);
    }

    @Cacheable(value = "home", unless = "#result == null")
    public List<FtHome> getHomes() {
        return homeMapper.selectList(null);
    }

    /**
     * 一个树通过一个下级id如何找到最上级的id
     *
     * @param homes 宿舍楼树
     * @param id    下级id
     * @return 最上级id
     */
    public Long getTopId(List<FtHome> homes, Long id) {
        FtHome home = homes.stream().filter(h -> h.getId().equals(id)).findAny().orElse(null);
        if (home == null) {
            return null;
        }
        if (home.getParentId().equals(0L)) {
            return home.getId();
        }
        return getTopId(homes, home.getParentId());
    }

    public FtHome getTopHome(List<FtHome> homes, Long id) {
        FtHome home = homes.stream().filter(h -> h.getId().equals(id)).findAny().orElse(null);
        if (home == null) {
            return null;
        }
        if (home.getParentId().equals(0L)) {
            return home;
        }
        return getTopHome(homes, home.getParentId());
    }

    private List<HomeResponse> buildTree(List<FtHome> homes, Long parentId, Map<Long, List<SysUser>> userMap) {
        List<HomeResponse> responses = Lists.newArrayList();
        for (FtHome home : homes) {
            if (home.getParentId().equals(parentId)) {
                assembleHomeResponse(homes, userMap, responses, home);
            }
        }
        return responses;
    }

    private void assembleHomeResponse(List<FtHome> homes, Map<Long, List<SysUser>> userMap, List<HomeResponse> responses, FtHome home) {
        HomeResponse response = new HomeResponse();
        BeanUtils.copyProperties(home, response);
        response.setUsers(userMap.get(home.getId()) != null ? userMap.get(home.getId()) : Lists.newArrayList());
        response.setChildren(buildTree(homes, home.getId(), userMap));
        responses.add(response);
    }

    private Map<Long, List<SysUser>> getUserMap() {
        //找到所有的用户
        List<UserHome> userHomes = userHomeMapper.getUserHomes();
        Map<Long, List<SysUser>> userMap = new ConcurrentHashMap<>();
        if (CollectionUtils.isNotEmpty(userHomes)) {
            Set<Long> userIds = userHomes.stream().map(UserHome::getUserId).collect(Collectors.toSet());
            List<SysUser> users = userMapper.selectUserByIds(userIds);
            userHomes.forEach(u -> {
                List<SysUser> sysUsers = userMap.get(u.getHomeId());
                if (CollectionUtils.isEmpty(sysUsers)) {
                    sysUsers = Lists.newArrayList();
                }
                users.stream().filter(user -> user.getUserId().equals(u.getUserId())).findAny().ifPresent(sysUsers::add);
                userMap.put(u.getHomeId(), sysUsers);
            });
        }
        return userMap;
    }

    /**
     * 发送消息和通知
     *
     * @param homeId       宿舍楼id
     * @param userId       用户id
     * @param operator     操作 true 增加 false 减少
     * @param sourceNumber 源数量
     * @param number       数量
     * @param flag         是否是桶 桶不需要减库存
     * @return 是否成功
     */
    public Boolean sendMessageAndNotices(Long homeId, Long userId, Boolean operator, Integer sourceNumber, Integer number, Boolean flag, Integer orderType) {
        //找该楼下面的管理员 发送消息
        List<UserHome> userHomes = userHomeMapper.selectByHomeId(homeId);

        if (CollectionUtils.isEmpty(userHomes)) {
            throw new SecurityException("该宿舍楼下没有管理员");
        }

        //消息
        FtMessage message = FtMessage.builder()
                .orderType(orderType)
                .operator(operator)
                .number(number)
                .homeId(homeId)
                .userId(StringUtils.join(userHomes.stream().map(UserHome::getUserId).collect(Collectors.toList()), ","))
                .confirm(0)
                .build();
        message.setCreateBy(userId.toString());
        message.setUpdateBy(userId.toString());
        message.setCreateTime(new Date());

        messageService.insertSelective(message);
//        log.info("发送通知{}条", messages.size());
        return addNotices(homeId, userId, number, operator, sourceNumber, flag, 0, orderType);
    }

    public Boolean addNotices(Long homeId,
                              Long userId,
                              Integer number,
                              Boolean operator,
                              Integer sourceNumber,
                              Boolean flag,
                              Integer type,
                              Integer orderType) {
        List<FtHome> homes = getHomes();
        Long topId = getTopId(homes, homeId);
        int total = 0;
        //如果是桶不需要减库存 2是驳回
        if (!flag && type != 2) {
            if (operator) {
                total = sourceNumber + number;
            } else {
                total = sourceNumber - number;
            }
        }

        if (type == 1 || type == 2) {
            List<UserHome> userHomes = userHomeMapper.selectByHomeId(homeId);
            if (CollectionUtils.isNotEmpty(userHomes)) {
                List<String> userIds = userHomes.stream().map(UserHome::getUserId).map(String::valueOf).collect(Collectors.toList());
                List<SysUser> users = userMapper.selectUsersByIds(userIds);
                users = users.stream().filter(u -> StringUtils.isNotEmpty(u.getOpenId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(users)) {
                    log.info("send message homeId:{},userIds:{}", homeId, userIds);
                    Map<String, Object> data = new HashMap<>();
                    data.put(type == 1 ? "thing4" : "thing6", new HashMap<String, String>() {{
                        put("value", "水"); // 替换为具体值
                    }});

                    if (type == 1) {
                        data.put("thing5", new HashMap<String, String>() {{
                            put("value", String.valueOf(number)); // 替换为具体值
                        }});
                    }

                    String name = homes.stream().filter(h -> h.getId().equals(homeId)).findFirst().orElse(new FtHome()).getName();
                    String topName = homes.stream().filter(h -> h.getId().equals(topId)).findFirst().orElse(new FtHome()).getName();
                    data.put(type == 1 ? "thing6" : "thing22", new HashMap<String, String>() {{
                        put("value", topName + "/" + name); // 替换为具体值
                    }});
                    data.put(type == 1 ? "time3" : "date2", new HashMap<String, String>() {{
                        put("value", DateUtils.getCurrentDate()); // 替换为具体值
                    }});
                    data.put(type == 1 ? "thing2" : "thing1", new HashMap<String, String>() {{
                        put("value", type == 1 ? "确认" : "驳回"); // 替换为具体值
                    }});

                    if (type == 2) {
                        data.put("phrase8", new HashMap<String, String>() {{
                            put("value", "驳回"); // 替换为具体值
                        }});
                    }

                    users.forEach(user -> WechatUtil.sendSubscriptionMessage(user.getOpenId(), String.valueOf(type), data));
                }
            }
        }

        FtNotices notices = FtNotices.builder()
                .type(type)
                .orderType(orderType)
                .homeId(homeId)
                //todo 需要逆向推算学校
                .schoolId(topId)
                .residue(total)
                .userId(userId)
                .number(number)
                .build();

        if (type == 2) {
            //取上一条的库存 必须是水
            FtNotices last = noticesService.selectLastByHomeIdAndOrderType(homeId, orderType);
            notices.setResidue(last == null ? 0 : last.getResidue());
        }

        notices.setCreateBy(userId.toString());
        notices.setUpdateBy(userId.toString());
        notices.setCreateTime(new

                Date());
        return noticesService.insertSelective(notices) > 0;
    }
}
