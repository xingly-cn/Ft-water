package com.ruoyi.web.controller.admin;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.request.HomeRequest;
import com.ruoyi.system.entity.FtHome;
import com.ruoyi.system.service.FtHomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/18 21:08
 */
@RestController
@RequestMapping("v1/admin/home")
@Api(tags = "后台-树")
public class HomeController {


    @Autowired
    private FtHomeService homeService;

    @GetMapping("/tree")
    @ApiOperation("树")
    public AjaxResult homeTree() {
        return AjaxResult.success(homeService.homeTree());
    }

    @PostMapping("/insert")
    @ApiOperation("新增树")
    public AjaxResult addHome(@RequestBody FtHome Home) {
        return AjaxResult.success(homeService.addHome(Home));
    }

    @PostMapping("/update")
    @ApiOperation("修改树")
    public AjaxResult updateHome(@RequestBody FtHome Home) {
        return AjaxResult.success(homeService.updateHome(Home));
    }

    @DeleteMapping("/remove")
    @ApiOperation("删除树")
    public AjaxResult delete(@RequestParam Long id) {
        return AjaxResult.success(homeService.deleteByPrimaryKey(id));
    }

    @GetMapping("/detail")
    @ApiOperation("商品详情")
    public AjaxResult getGoodsDetail(@RequestParam(value = "id") Long id) {
        return AjaxResult.success(homeService.selectByPrimaryKey(id));
    }

    @PostMapping("/addNumber")
    @ApiOperation("添加水漂数量")
    public AjaxResult addNumber(@RequestBody HomeRequest request) {
        return AjaxResult.success(homeService.addNumber(request));
    }

    @PostMapping("/addUser")
    @ApiOperation("添加管理员")
    public AjaxResult addUser(@RequestBody HomeRequest request) {
        return AjaxResult.success(homeService.addUser(request));
    }
}
