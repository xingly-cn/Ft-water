package com.ruoyi.web.controller.admin;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.entity.FtUser;
import com.ruoyi.system.request.LoginRequest;
import com.ruoyi.system.request.UserRequest;
import com.ruoyi.system.response.UserResponse;
import com.ruoyi.system.service.FtUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Desc 用户
 * @Author 方糖
 * @Date 2023-03-30 15:38
 **/
@RestController
@RequestMapping("v1/admin/user")
@Api(tags = "后台-用户")
public class AdminUserController extends BaseController {

    @Resource
    private FtUserService userService;

    @GetMapping("/page")
    @ApiOperation("用户列表-分页")
    public TableDataInfo getUserList(UserRequest userRequest) {
        startPage();
        List<UserResponse> userResponses = userService.getUserList(userRequest);
        return getDataTable(userResponses);
    }

    @GetMapping("/search")
    @ApiOperation("用户搜索-缩写")
    public AjaxResult getUserBySearch(String str) {
        return AjaxResult.success(userService.getUserBySearch(str));
    }

    @GetMapping("/coupon")
    @ApiOperation("券包")
    public AjaxResult getUserDetail(HttpServletRequest request) {
        return AjaxResult.success(userService.checkCoupon(request));
    }

    @GetMapping("/detail")
    @ApiOperation("用户详情")
    public AjaxResult getUserDetail(@RequestParam(value = "id") Long id) {
        return AjaxResult.success(userService.selectByPrimaryKey(id));
    }

    @PostMapping("/insert")
    @ApiOperation("新增用户")
    public AjaxResult addUser(@RequestBody FtUser User) {
        return AjaxResult.success(userService.addUser(User));
    }

    @PostMapping("/update")
    @ApiOperation("修改用户")
    public AjaxResult updateUser(@RequestBody FtUser User) {
        return AjaxResult.success(userService.updateUser(User));
    }

    @GetMapping("/list")
    @ApiOperation("用户列表")
    public AjaxResult selectUserList(UserRequest request) {
        return AjaxResult.success(userService.getUserList(request));
    }

    @DeleteMapping("/remove")
    @ApiOperation("删除用户")
    public AjaxResult delete(@RequestParam Long id) {
        return AjaxResult.success(userService.deleteByPrimaryKey(id));
    }

    @PostMapping("/login")
    @ApiOperation("登陆")
    public AjaxResult login(@RequestBody LoginRequest request) {
        return AjaxResult.success(userService.login(request));
    }

    @PostMapping("/change")
    @ApiOperation("修改密码")
    public AjaxResult change(@RequestBody UserRequest request) {
        return AjaxResult.success(userService.change(request));
    }
}
