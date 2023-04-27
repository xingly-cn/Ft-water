package com.ruoyi.system.service;


import com.ruoyi.system.entity.FtUser;
import com.ruoyi.system.request.LoginRequest;
import com.ruoyi.system.request.UserRequest;
import com.ruoyi.system.request.WechatUserInfo;
import com.ruoyi.system.response.UserResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
* Created by IntelliJ IDEA.
* @Author : 镜像
* @create 2023/3/30 15:59
*/
public interface FtUserService{

    Boolean deleteByPrimaryKey(Long id);

    Boolean addUser(FtUser record);

    FtUser selectByPrimaryKey(Long id);

    Boolean updateUser(FtUser record);

    Map<String, Object> getUserPage(UserRequest userRequest);

    List<FtUser> selectUserList(FtUser User);

    UserResponse login(LoginRequest request);

    UserResponse loginUser(LoginRequest request);

    String decryptPhone(String encryptedData, String iv, String sessionKey);

    FtUser checkPhone(String phone, String code);

    Boolean updateUserInfo(WechatUserInfo userInfo);

    String change(UserRequest request);

    String updateAddress(UserRequest request);

    String getPhone(UserRequest request);

    String changeUserPhone(UserRequest request);

    FtUser getUserInfo(HttpServletRequest rq);

    List<FtUser> getUserBySearch(String str);

    Object checkCoupon(HttpServletRequest request);
}
