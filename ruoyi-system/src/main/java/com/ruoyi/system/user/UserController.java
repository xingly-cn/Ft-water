package com.ruoyi.system.user;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.request.LoginRequest;
import com.ruoyi.system.request.UserRequest;
import com.ruoyi.system.request.WechatUserInfo;
import com.ruoyi.system.service.ISysUserService;
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
    private ISysUserService userService;

    @PostMapping("/login")
    @ApiOperation("登陆")
    public AjaxResult login(@RequestBody LoginRequest request) {
        // 学生使用微信登陆, 所以没有密码, 直接生成token
        return AjaxResult.success(userService.loginUser(request));
    }

    @ApiOperation(value = "获取手机号-新版小程序")
    @PostMapping(value = "/getPhone")
    public AjaxResult getPhone(@RequestBody UserRequest request) {
        System.out.println("传入的参数为:" + request);
        return AjaxResult.success(userService.getPhone(request));
    }


    @ApiOperation(value = "检查手机号-是否存在-用于区分用户类型-后台注册更新openId")
    @PostMapping(value = "/checkPhone")
    public AjaxResult checkPhone(String phone, String code) {
        return AjaxResult.success(userService.checkPhone(phone, code));
    }

    @PostMapping("/userInfo")
    @ApiOperation("更新用户信息")
    public AjaxResult updateUserInfo(@RequestBody WechatUserInfo userInfo) {
        return AjaxResult.success(userService.updateUserInfo(userInfo));
    }

    @PostMapping("/updatePhone")
    @ApiOperation("更新用户手机")
    public AjaxResult changeUserPhone(@RequestBody UserRequest request) {
        return AjaxResult.success(userService.changeUserPhone(request));
    }

    @GetMapping("/getUserInfo")
    @ApiOperation("获取用户信息")
    public AjaxResult getUserInfo(HttpServletRequest rq) {
        return AjaxResult.success(userService.getUserInfo(rq));
    }
}
