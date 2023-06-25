package com.ruoyi.system.service;

import com.ruoyi.system.domain.UserHome;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/5/1 15:35
 */
public interface UserHomeService {

    Boolean deleteUserHomeByUserIdAndHomeId(Long userId, Long homeId);

    List<Long> selectHomeIdByUserId(Long userId);

    List<UserHome> selectAllUserHomes(Boolean flag,Long userId);
}
