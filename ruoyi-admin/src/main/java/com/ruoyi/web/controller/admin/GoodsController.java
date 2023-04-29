package com.ruoyi.web.controller.admin;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.request.GoodRequest;
import com.ruoyi.system.entity.FtGoods;
import com.ruoyi.system.response.GoodsResponse;
import com.ruoyi.system.service.FtGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Desc 商品
 * @Author 方糖
 * @Date 2023-03-30 15:38
 **/
@RestController
@RequestMapping("v1/admin/goods")
@Api(tags = "后台-商品")
public class GoodsController extends BaseController {

    @Resource
    private FtGoodsService goodsService;

    @GetMapping("/page")
    @ApiOperation("商品列表-已上架")
    public TableDataInfo getGoodsList(GoodRequest goodRequest) {
        startPage();
        List<GoodsResponse> responses = goodsService.selectGoodsList(goodRequest);
        return getDataTable(responses);
    }


    @GetMapping("/detail")
    @ApiOperation("商品详情")
    public AjaxResult getGoodsDetail(@RequestParam(value = "id") Long id) {
        return AjaxResult.success(goodsService.selectByPrimaryKey(id));
    }

    @PostMapping("/insert")
    @ApiOperation("商品上架")
    public AjaxResult addGoods(@RequestBody FtGoods Goods) {
        return AjaxResult.success(goodsService.addGoods(Goods));
    }

    @PostMapping("/update")
    @ApiOperation("修改商品")
    public AjaxResult updateGoods(@RequestBody FtGoods Goods) {
        return AjaxResult.success(goodsService.updateGoods(Goods));
    }

    @GetMapping("/list")
    @ApiOperation("商品列表")
    public AjaxResult selectGoodsList(GoodRequest request) {
        return AjaxResult.success(goodsService.selectGoodsList(request));
    }

    @DeleteMapping("/remove")
    @ApiOperation("商品下架")
    public AjaxResult delete(@RequestParam Long id) {
        return AjaxResult.success(goodsService.deleteByPrimaryKey(id));
    }
}
