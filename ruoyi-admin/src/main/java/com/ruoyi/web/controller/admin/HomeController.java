package com.ruoyi.web.controller.admin;


import com.ruoyi.web.controller.request.HomeRequest;
import com.ruoyi.web.entity.FtHome;
import com.ruoyi.web.service.FtHomeService;
import com.ruoyi.web.utils.Result;
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
    public Result<?> homeTree() {
        return Result.success(homeService.homeTree());
    }

    @PostMapping("/insert")
    @ApiOperation("新增树")
    public Result<?> addHome(@RequestBody FtHome Home) {
        return Result.success(homeService.addHome(Home));
    }

    @PostMapping("/update")
    @ApiOperation("修改树")
    public Result<?> updateHome(@RequestBody FtHome Home) {
        return Result.success(homeService.updateHome(Home));
    }

    @DeleteMapping("/remove")
    @ApiOperation("删除树")
    public Result<?> delete(@RequestParam Long id) {
        return Result.success(homeService.deleteByPrimaryKey(id));
    }

    @GetMapping("/detail")
    @ApiOperation("商品详情")
    public Result<?> getGoodsDetail(@RequestParam(value = "id") Long id) {
        return Result.success(homeService.selectByPrimaryKey(id));
    }

    @PostMapping("/addNumber")
    @ApiOperation("添加水漂数量")
    public Result<?> addNumber(@RequestBody HomeRequest request) {
        return Result.success(homeService.addNumber(request));
    }

    @PostMapping("/addUser")
    @ApiOperation("添加管理员")
    public Result<?> addUser(@RequestBody HomeRequest request) {
        return Result.success(homeService.addUser(request));
    }
}
