package com.ruoyi.web.controller.admin;


import com.ruoyi.web.controller.request.SaleRequest;
import com.ruoyi.web.entity.FtSale;
import com.ruoyi.web.service.FtSaleService;
import com.ruoyi.web.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Desc 核销
 * @Author 方糖
 * @Date 2023-03-30 15:38
 **/
@RestController
@RequestMapping("v1/admin/sale")
@Api(tags = "后台-核销")
public class SaleController {

    @Resource
    private FtSaleService saleService;

    @GetMapping("/page")
    @ApiOperation("核销列表-分页")
    public Result<?> getSaleList(SaleRequest request) {
        return Result.success(saleService.getSalePage(request));
    }

    @GetMapping("/detail")
    @ApiOperation("核销详情")
    public Result<?> getSaleDetail(@RequestParam(value = "id") Long id) {
        return Result.success(saleService.selectByPrimaryKey(id));
    }

    @PostMapping("/insert")
    @ApiOperation("新增核销")
    public Result<?> addSale(@RequestBody FtSale Sale) {
        return Result.success(saleService.addSale(Sale));
    }

    @PostMapping("/update")
    @ApiOperation("修改核销")
    public Result<?> updateSale(@RequestBody FtSale Sale) {
        return Result.success(saleService.updateSale(Sale));
    }

    @GetMapping("/list")
    @ApiOperation("核销列表")
    public Result<?> selectSaleList(SaleRequest Sale) {
        return Result.success(saleService.selectSaleList(Sale));
    }

    @DeleteMapping("/remove")
    @ApiOperation("删除核销")
    public Result<?> delete(@RequestParam Long id) {
        return Result.success(saleService.deleteByPrimaryKey(id));
    }
}
