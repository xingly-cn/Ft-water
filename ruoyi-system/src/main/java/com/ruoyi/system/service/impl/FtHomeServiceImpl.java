package com.ruoyi.system.service.impl;


import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.entity.FtHome;
import com.ruoyi.system.entity.FtMessage;
import com.ruoyi.system.entity.FtNotices;
import com.ruoyi.system.entity.FtUser;
import com.ruoyi.system.mapper.FtHomeMapper;
import com.ruoyi.system.mapper.FtUserMapper;
import com.ruoyi.system.request.HomeRequest;
import com.ruoyi.system.response.HomeResponse;
import com.ruoyi.system.service.FtHomeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
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
    private FtUserMapper userMapper;

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
        List<FtUser> users = userMapper.getUsersFindByType(1);
        Map<String, List<FtUser>> userMap = new ConcurrentHashMap<>();
        if (CollectionUtils.isNotEmpty(users)) {
            userMap = users.stream().collect(Collectors.groupingBy(FtUser::getHomeId));
        }

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
        //找该楼下面的管理员 发送消息
        List<FtUser> users = userMapper.getUserByHomeId(request.getId(), 1);


        // todo，等user好了，在处理，先模拟一个user
//        if (CollectionUtils.isEmpty(users)) {
//            throw new SecurityException("该宿舍楼下没有管理员");
//        }

        //消息
        List<FtMessage> messages = Lists.newArrayList();
        for (FtUser user : users) {

            // todo 模拟
            user.setId(1L);

            FtMessage message = FtMessage.builder()
                    .number(request.getNumber())
                    .homeId(request.getId())
                    .userId(user.getId())
                    .confirm(false)
                    .build();
            message.setCreateBy(userId.toString());
            message.setUpdateBy(userId.toString());
            message.setCreateTime(new Date());
            messages.add(message);
        }
        messageService.addMessages(messages);
        log.info("发送通知{}条", messages.size());
        //日志 记录
        //xxx学校xxx宿舍楼xxx用户xxx手机几点提货多少 - 剩余多少
        //xxx学校xxx宿舍楼几点收货多少 - 剩余多少
        List<FtHome> homes = getHomes();
        Long topId = getTopId(homes, home.getId());
        FtNotices notices = FtNotices.builder()
                .type(0)
                .homeId(request.getId())
                //todo 需要逆向推算学校
                .schoolId(topId)
                .userId(userId)
                .number(request.getNumber())
                .residue(home.getNumber() + request.getNumber())
                .build();
        notices.setCreateBy(userId.toString());
        notices.setUpdateBy(userId.toString());
        notices.setCreateTime(new Date());
        noticesService.insert(notices);
        return true;
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
        return null;
    }

    @Override
    public Boolean addUser(HomeRequest request) {
        HomeResponse home = selectByPrimaryKey(request.getId());
        if (home == null) {
            throw new SecurityException("该宿舍楼不存在");
        }
        //是否存在
        FtUser user = userMapper.selectByPrimaryKey(request.getUserId());
        if (user == null) {
            throw new SecurityException("该用户不存在");
        }

        //查询该用户是否在其他宿舍楼下面是管理员
        List<FtUser> myUsers = userMapper.getUserByUserId(request.getUserId(), 1);
        if (CollectionUtils.isNotEmpty(myUsers)) {
            throw new SecurityException("该用户已经是其他宿舍楼管理员了");
        }

        //查询该宿舍楼下面的管理员 最多俩个 一个人只可以管理一个宿舍楼
        List<FtUser> users = userMapper.getUserByHomeId(request.getId(), 1);
        if (users.size() >= 2) {
            throw new SecurityException("该宿舍楼下面已经有两个管理员了，不能再添加了");
        }
        //查询该用户是否已经是管理员
        users.stream().filter(u -> u.getId().equals(request.getUserId())).findAny().ifPresent(u -> {
            throw new SecurityException("该用户已经是该宿舍楼管理员了");
        });
        //添加管理员 更新用户 type 以及 楼栋id
        return userMapper.updateUserHomeId(request.getUserId(), request.getId());
    }

    private List<HomeResponse> buildTree(List<FtHome> homes, Long parentId, Map<String, List<FtUser>> userMap) {
        List<HomeResponse> responses = Lists.newArrayList();
        for (FtHome home : homes) {
            if (home.getParentId().equals(parentId)) {
                assembleHomeResponse(homes, userMap, responses, home);
            }
        }
        return responses;
    }

    private void assembleHomeResponse(List<FtHome> homes, Map<String, List<FtUser>> userMap, List<HomeResponse> responses, FtHome home) {
        HomeResponse response = new HomeResponse();
        BeanUtils.copyProperties(home, response);
        response.setUsers(userMap.get(home.getId().toString()) != null ? userMap.get(home.getId().toString()) : Lists.newArrayList());
        response.setChildren(buildTree(homes, home.getId(), userMap));
        responses.add(response);
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

    public String getSchoolByRemark(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return homeMapper.getSchoolByRemark(name);
    }

    @Cacheable(value = "home",unless = "#result == null")
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
    private Long getTopId(List<FtHome> homes, Long id) {
        FtHome home = homes.stream().filter(h -> h.getId().equals(id)).findAny().orElse(null);
        if (home == null) {
            return null;
        }
        if (home.getParentId().equals(0L)) {
            return home.getId();
        }
        return getTopId(homes, home.getParentId());
    }
}
