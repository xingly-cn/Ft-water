package com.ruoyi.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.web.controller.request.UserRequest;
import com.ruoyi.web.controller.response.UserResponse;
import com.ruoyi.system.entity.FtUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* Created by IntelliJ IDEA.
* @Author : 镜像
* @create 2023/3/30 15:59
*/
@Repository
public interface FtUserMapper extends BaseMapper<FtUser> {

    int deleteByPrimaryKey(Long id);

    int insertSelective(FtUser record);

    FtUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FtUser record);

    int updateByPrimaryKey(FtUser record);

    int updatePasswordByPhone(@Param("password") String password,
                              @Param("phone") String phone);

    void updateOpenIdByPhone(@Param("openId") String openId,
                             @Param("phone") String phone);

    int updateAddressByPhone(@Param("address") String address,
                             @Param("phone") String phone);

    List<FtUser> getUserByHomeId(@Param("homeId") Long homeId, @Param("type") Integer type);

    List<FtUser> getUserByUserId(@Param("userId") Long userId, @Param("type") Integer type);

    Boolean updateUserHomeId(@Param("userId") Long userId, @Param("homeId") Long homeId);

    List<FtUser> getUsersFindByType(@Param("type") Integer type);

    Page<UserResponse> selectPage(@Param("page") IPage<UserResponse> page,
                                  @Param("user") UserRequest request);
}