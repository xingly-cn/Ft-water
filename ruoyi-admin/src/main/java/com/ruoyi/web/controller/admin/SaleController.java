package com.ruoyi.web.controller.admin;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.request.SaleRequest;
import com.ruoyi.system.domain.FtSale;
import com.ruoyi.system.response.SaleResponse;
import com.ruoyi.system.service.FtSaleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Desc 核销
 * @Author 方糖
 * @Date 2023-03-30 15:38
 **/
@RestController
@RequestMapping("v1/admin/sale")
@Api(tags = "后台-核销")
public class SaleController extends BaseController {

    @Resource
    private FtSaleService saleService;

    @GetMapping("/page")
    @ApiOperation("核销列表-分页")
    public TableDataInfo getSaleList(SaleRequest request) {
        startPage();
        List<SaleResponse> responses = saleService.selectSaleList(request);
        return getDataTable(responses);
    }

    @GetMapping("/detail")
    @ApiOperation("核销详情")
    public AjaxResult getSaleDetail(@RequestParam(value = "id") Long id) {
        return AjaxResult.success(saleService.selectByPrimaryKey(id));
    }

    @PostMapping("/insert")
    @ApiOperation("新增核销")
    public AjaxResult addSale(@RequestBody FtSale Sale) {
        return AjaxResult.success(saleService.addSale(Sale));
    }

    @PostMapping("/update")
    @ApiOperation("修改核销")
    public AjaxResult updateSale(@RequestBody FtSale Sale) {
        return AjaxResult.success(saleService.updateSale(Sale));
    }

    @GetMapping("/list")
    @ApiOperation("核销列表")
    public AjaxResult selectSaleList(SaleRequest Sale) {
        return AjaxResult.success(saleService.selectSaleList(Sale));
    }

    @DeleteMapping("/remove")
    @ApiOperation("删除核销")
    public AjaxResult delete(@RequestParam Long id) {
        return AjaxResult.success(saleService.deleteByPrimaryKey(id));
    }
}
