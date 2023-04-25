package com.ruoyi.web.controller.user;


import com.asugar.ftwaterdelivery.utils.Result;
import com.ruoyi.web.controller.request.LoginRequest;
import com.ruoyi.web.controller.request.UserRequest;
import com.ruoyi.web.controller.request.WechatUserInfo;
import com.ruoyi.web.service.FtUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Desc 用户
 * @Author 方糖
 * @Date 2023-03-30 15:37
 **/
@RestController
@RequestMapping("v1/user/user")
@Api(tags = "微信用户")
public class UserController {

    @Resource
    private FtUserService userService;

    @PostMapping("/login")
    @ApiOperation("登陆")
    public Result<?> login(@RequestBody LoginRequest request) {
        // 学生使用微信登陆, 所以没有密码, 直接生成token
        return Result.success(userService.loginUser(request));
    }

    @ApiOperation(value = "解密手机号-老版小程序")
    @PostMapping(value = "/decryptPhone")
    public Result<?> decryptPhone(String encryptedData, String iv, String sessionKey) {
        return Result.success(userService.decryptPhone(encryptedData, iv, sessionKey));
    }

    @ApiOperation(value = "获取手机号-新版小程序")
    @PostMapping(value = "/getPhone")
    public Result<?> getPhone(@RequestBody UserRequest request) {
        System.out.println("传入的参数为:" + request);
        return Result.success(userService.getPhone(request));
    }


    @ApiOperation(value = "检查手机号-是否存在-用于区分用户类型-后台注册更新openId")
    @PostMapping(value = "/checkPhone")
    public Result<?> checkPhone(String phone, String code) {
        return Result.success(userService.checkPhone(phone, code));
    }

    @PostMapping("/userInfo")
    @ApiOperation("更新用户信息")
    public Result<?> updateUserInfo(@RequestBody WechatUserInfo userInfo) {
        return Result.success(userService.updateUserInfo(userInfo));
    }

    @PostMapping("/updatePhone")
    @ApiOperation("更新用户手机")
    public Result<?> changeUserPhone(@RequestBody UserRequest request) {
        return Result.success(userService.changeUserPhone(request));
    }

    @GetMapping("/getUserInfo")
    @ApiOperation("获取用户信息")
    public Result<?> getUserInfo(HttpServletRequest rq) {
        return Result.success(userService.getUserInfo(rq));
    }
}
