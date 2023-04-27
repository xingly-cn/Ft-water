package com.ruoyi.web.controller.admin;


import com.ruoyi.system.request.GoodRequest;
import com.ruoyi.system.entity.FtGoods;
import com.ruoyi.system.service.FtGoodsService;
import com.ruoyi.system.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Desc 商品
 * @Author 方糖
 * @Date 2023-03-30 15:38
 **/
@RestController
@RequestMapping("v1/admin/goods")
@Api(tags = "后台-商品")
public class GoodsController {

    @Resource
    private FtGoodsService goodsService;

    @GetMapping("/page")
    @ApiOperation("商品列表-已上架")
    public Result<?> getGoodsList(GoodRequest goodRequest) {
        return Result.success(goodsService.getGoodsPage(goodRequest));
    }


    @GetMapping("/detail")
    @ApiOperation("商品详情")
    public Result<?> getGoodsDetail(@RequestParam(value = "id") Long id) {
        return Result.success(goodsService.selectByPrimaryKey(id));
    }

    @PostMapping("/insert")
    @ApiOperation("商品上架")
    public Result<?> addGoods(@RequestBody FtGoods Goods) {
        return Result.success(goodsService.addGoods(Goods));
    }

    @PostMapping("/update")
    @ApiOperation("修改商品")
    public Result<?> updateGoods(@RequestBody FtGoods Goods) {
        return Result.success(goodsService.updateGoods(Goods));
    }

    @GetMapping("/list")
    @ApiOperation("商品列表")
    public Result<?> selectGoodsList(FtGoods Goods) {
        return Result.success(goodsService.selectGoodsList(Goods));
    }

    @DeleteMapping("/remove")
    @ApiOperation("商品下架")
    public Result<?> delete(@RequestParam Long id) {
        return Result.success(goodsService.deleteByPrimaryKey(id));
    }
}
