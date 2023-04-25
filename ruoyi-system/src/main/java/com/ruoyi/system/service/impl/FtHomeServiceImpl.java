package com.ruoyi.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.system.entity.*;
import com.ruoyi.system.mapper.FtHomeMapper;
import com.ruoyi.system.mapper.FtUserMapper;
import com.ruoyi.system.request.HomeRequest;
import com.ruoyi.system.response.HomeResponse;
import com.ruoyi.system.service.FtHomeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
public class FtHomeServiceImpl extends BaseMapperImpl<FtHome, HomeResponse, HomeRequest, FtHomeMapper> implements FtHomeService {

    @Autowired
    private FtHomeMapper homeMapper;

    @Autowired
    private FtSchoolServiceImpl schoolService;

    @Autowired
    private FtUserMapper userMapper;

    @Autowired
    private FtMessageServiceImpl messageService;

    @Autowired
    private FtNoticesServiceImpl noticesService;

    @Override
    public Boolean deleteByPrimaryKey(Long id) {
        return homeMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public Boolean addHome(FtHome record) {
        return homeMapper.insertSelective(record) > 0;
    }

    @Override
    public Boolean updateHome(FtHome record) {
        return homeMapper.updateByPrimaryKeySelective(record) > 0;
    }

    @Override
    public List<HomeResponse> homeTree() {
        List<HomeResponse> responses = Lists.newArrayList();

        List<FtHome> homes = homeMapper.selectList(new QueryWrapper<>());
        if (CollectionUtils.isEmpty(homes)) {
            return responses;
        }

        //找到所有的用户
        List<FtUser> users = userMapper.getUsersFindByType(1);
        Map<String, List<FtUser>> userMap = new ConcurrentHashMap<>();
        if (CollectionUtils.isNotEmpty(users)) {
            userMap = users.stream().collect(Collectors.groupingBy(FtUser::getHomeId));
        }

        //找到所有的学校
        List<Long> schoolIds = homes.stream().map(FtHome::getSchoolId).distinct().collect(Collectors.toList());
        List<FtSchool> schools = schoolService.selectSchoolListByIds(schoolIds);
        Map<Long, String> schoolMap = schools.stream().collect(Collectors.toMap(FtSchool::getId, FtSchool::getSchoolName));

        //先找到所有的一级菜单
        List<FtHome> parentHomes = homes.stream().filter(h -> h.getParentId().equals(0L)).collect(Collectors.toList());
        for (FtHome parentHome : parentHomes) {
            assembleHomeReponse(homes, schoolMap, userMap, responses, parentHome);
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
        Long userId = Long.parseLong(getCurrentUser().get("userId"));
        //找该楼下面的管理员 发送消息
        List<FtUser> users = userMapper.getUserByHomeId(request.getId(), 1);

        if (CollectionUtils.isEmpty(users)) {
            throw new SecurityException("该宿舍楼下没有管理员");
        }
        //消息
        List<FtMessage> messages = Lists.newArrayList();
        for (FtUser user : users) {
            messages.add(FtMessage.builder()
                    .number(request.getNumber())
                    .homeId(request.getId())
                    .userId(user.getId())
                    .confirm(false)
                    .createBy(userId)
                    .updateBy(userId)
                    .createTime(LocalDateTime.now())
                    .build());
        }
        messageService.addMessages(messages);
        log.info("发送通知{}条", messages.size());
        //日志 记录
        //xxx学校xxx宿舍楼xxx用户xxx手机几点提货多少 - 剩余多少
        //xxx学校xxx宿舍楼几点收货多少 - 剩余多少
        FtNotices notices = FtNotices.builder()
                .type(0)
                .homeId(request.getId())
                .schoolId(home.getSchoolId())
                .userId(userId)
                .number(request.getNumber())
                .residue(home.getNumber() + request.getNumber())
                .createTime(LocalDateTime.now())
                .createBy(userId)
                .updateBy(userId)
                .build();
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
            FtSchool school = schoolService.selectByPrimaryKey(home.getSchoolId());
            response.setSchoolName(school.getSchoolName());
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

    private List<HomeResponse> buildTree(List<FtHome> homes, Long parentId, Map<Long, String> schoolMap,Map<String, List<FtUser>> userMap) {
        List<HomeResponse> responses = Lists.newArrayList();
        for (FtHome home : homes) {
            if (home.getParentId().equals(parentId)) {
                assembleHomeReponse(homes, schoolMap, userMap, responses, home);
            }
        }
        return responses;
    }

    private void assembleHomeReponse(List<FtHome> homes, Map<Long, String> schoolMap, Map<String, List<FtUser>> userMap, List<HomeResponse> responses, FtHome home) {
        HomeResponse response = new HomeResponse();
        BeanUtils.copyProperties(home, response);
        response.setSchoolName(schoolMap.get(home.getSchoolId()));
        response.setUsers(userMap.get(home.getId().toString()) != null ? userMap.get(home.getId().toString()) : Lists.newArrayList());
        response.setChildren(buildTree(homes, home.getId(), schoolMap,userMap));
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

    @Override
    protected void customSelectPage(IPage<HomeResponse> page, HomeRequest request) {

    }
}
