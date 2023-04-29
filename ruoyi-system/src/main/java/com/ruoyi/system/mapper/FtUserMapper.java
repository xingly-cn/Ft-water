package com.ruoyi.system.mapper;


import com.ruoyi.system.entity.FtUser;
import com.ruoyi.system.request.UserRequest;
import com.ruoyi.system.response.UserResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/30 15:59
 */
@Repository
public interface FtUserMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(FtUser record);

    FtUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FtUser record);

    int updateByPrimaryKey(FtUser record);

    void updateOpenIdByPhone(@Param("openId") String openId,
                             @Param("phone") String phone);

    int updateAddressByPhone(@Param("address") String address,
                             @Param("phone") String phone);

    List<FtUser> getUserByHomeId(@Param("homeId") Long homeId, @Param("type") Integer type);

    List<FtUser> getUserByUserId(@Param("userId") Long userId, @Param("type") Integer type);

    Boolean updateUserHomeId(@Param("userId") Long userId, @Param("homeId") Long homeId);

    List<FtUser> getUsersFindByType(@Param("type") Integer type);

    List<UserResponse> selectList(@Param("user") UserRequest request);

    List<UserResponse> getUserList(@Param("user") UserRequest userRequest);

    FtUser getUserByPhone(String phone);

    FtUser getUserByOpenId(String openId);
}