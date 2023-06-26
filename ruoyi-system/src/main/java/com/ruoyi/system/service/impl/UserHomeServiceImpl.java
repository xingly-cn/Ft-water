package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.domain.UserHome;
import com.ruoyi.system.exception.ServiceException;
import com.ruoyi.system.mapper.UserHomeMapper;
import com.ruoyi.system.service.UserHomeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/5/1 15:36
 */
@Service
public class UserHomeServiceImpl implements UserHomeService {

    @Autowired
    private UserHomeMapper userHomeMapper;

    @Autowired
    private SysRoleServiceImpl roleService;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "userHome_userId", key = "#userId"),
            @CacheEvict(value = "userHome_id", key = "#homeId")
    })
    public Boolean deleteUserHomeByUserIdAndHomeId(Long userId, Long homeId) {
        //检查这个用户是几个宿舍的管理员
        List<UserHome> userHomes = userHomeMapper.selectByUserId(userId);
        if (CollectionUtils.isEmpty(userHomes)) {
            throw new ServiceException("该用户不是任何宿舍的管理员");
        }
        //检查这个用户是不是这个宿舍的管理员
        long count = userHomes.stream().filter(userHome -> userHome.getHomeId().equals(homeId)).count();
        if (count == 0) {
            throw new ServiceException("该用户不是该宿舍的管理员");
        }
        //删除
        if (userHomes.size()==1){
            //需要删除用户与角色的关系
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(7L);
            roleService.deleteAuthUser(userRole);
        }
        return userHomeMapper.deleteUserHomeByUserIdAndHomeId(userId, homeId);
    }

    @Override
    @Cacheable(value = "userHome_userId", key = "#userId")
    public Set<Long> selectHomeIdByUserId(Long userId) {
        return userHomeMapper.selectHomeIdByUserId(userId);
    }

    @Override
    public List<UserHome> selectAllUserHomes(Boolean flag,Long userId) {
        return userHomeMapper.selectAllUserHomes(flag,userId);
    }

    @Cacheable(value = "userHome_id", key = "#id")
    public List<UserHome> selectByHomeId(Long id) {
        return userHomeMapper.selectByHomeId(id);
    }

    @Caching(put = {
            @CachePut(value = "userHome_userId", key = "#userHome.userId"),
            @CachePut(value = "userHome_id", key = "#userHome.homeId")
    })
    public void insert(UserHome userHome) {
        userHomeMapper.insert(userHome);
    }

    public List<UserHome> getUserHomes() {
        return userHomeMapper.getUserHomes();
    }
}
