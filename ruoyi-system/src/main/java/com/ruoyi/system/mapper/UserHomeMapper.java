package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserHome;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/5/1 15:17
 */
@Repository
public interface UserHomeMapper {

    boolean deleteUserHomeByUserIdAndHomeId(@Param("userId") Long userId,
                                            @Param("homeId") Long homeId);

    void batchUserHome(List<UserHome> userHomes);

    List<UserHome> selectByHomeId(@Param("homeId") Long homeId);

    void insert(UserHome userHome);

    List<UserHome> selectByUserId(Long userId);

    List<UserHome> getUserHomes();

    List<Integer> selectHomeIdByUserId(Long userId);

    List<Integer> selectAllHomeId();

    List<Integer> selectAllUserId();
}
