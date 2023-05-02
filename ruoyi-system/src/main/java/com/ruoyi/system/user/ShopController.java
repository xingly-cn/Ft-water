package com.ruoyi.system.user;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.request.ShopRequest;
import com.ruoyi.system.response.ShopResponse;
import com.ruoyi.system.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/5/2 17:13
 */
@RestController
@Api(tags = "购物车")
public class ShopController extends BaseController {

    @Autowired
    private ShopService shopService;

    @PostMapping("/insert")
    @ApiOperation("加入")
    public AjaxResult insertShop(@RequestBody ShopRequest request) {
        return AjaxResult.success(shopService.insertShop(request));
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public AjaxResult updateShop(@RequestBody ShopRequest request) {
        return AjaxResult.success(shopService.updateShop(request));
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除")
    public AjaxResult deleteShop(@RequestParam(value = "id") Long id) {
        return AjaxResult.success(shopService.deleteShop(id));
    }

    @GetMapping("/list")
    @ApiOperation("获取当前用户购物车列表")
    public TableDataInfo getShopList(ShopRequest request) {
        startPage();
        List<ShopResponse> ShopList = shopService.getShopList(request);
        return getDataTable(ShopList);
    }

    @GetMapping("/detail")
    @ApiOperation("详情")
    public AjaxResult getShopDetail(Long id) {
        return AjaxResult.success(shopService.getShopDetail(id));
    }
}
