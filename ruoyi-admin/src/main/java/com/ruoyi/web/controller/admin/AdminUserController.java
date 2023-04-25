package com.ruoyi.web.controller.admin;


import com.ruoyi.web.controller.request.LoginRequest;
import com.ruoyi.web.controller.request.UserRequest;
import com.ruoyi.web.entity.FtUser;
import com.ruoyi.web.service.FtUserService;
import com.ruoyi.web.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Desc 用户
 * @Author 方糖
 * @Date 2023-03-30 15:38
 **/
@RestController
@RequestMapping("v1/admin/user")
@Api(tags = "后台-用户")
public class AdminUserController {

    @Resource
    private FtUserService userService;

    @GetMapping("/page")
    @ApiOperation("用户列表-分页")
    public Result<?> getUserList(UserRequest userRequest) {
        return Result.success(userService.getUserPage(userRequest));
    }

    @GetMapping("/detail")
    @ApiOperation("用户详情")
    public Result<?> getUserDetail(@RequestParam(value = "id") Long id) {
        return Result.success(userService.selectByPrimaryKey(id));
    }

    @PostMapping("/insert")
    @ApiOperation("新增用户")
    public Result<?> addUser(@RequestBody FtUser User) {
        return Result.success(userService.addUser(User));
    }

    @PostMapping("/update")
    @ApiOperation("修改用户")
    public Result<?> updateUser(@RequestBody FtUser User) {
        return Result.success(userService.updateUser(User));
    }

    @GetMapping("/list")
    @ApiOperation("用户列表")
    public Result<?> selectUserList(FtUser User) {
        return Result.success(userService.selectUserList(User));
    }

    @DeleteMapping("/remove")
    @ApiOperation("删除用户")
    public Result<?> delete(@RequestParam Long id) {
        return Result.success(userService.deleteByPrimaryKey(id));
    }

    @PostMapping("/login")
    @ApiOperation("登陆")
    public Result<?> login(@RequestBody LoginRequest request) {
        return Result.success(userService.login(request));
    }

    @PostMapping("/change")
    @ApiOperation("修改密码")
    public Result<?> change(@RequestBody UserRequest request) {
        return Result.success(userService.change(request));
    }
}
