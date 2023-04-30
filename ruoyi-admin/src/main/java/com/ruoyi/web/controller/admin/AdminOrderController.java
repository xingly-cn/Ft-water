package com.ruoyi.web.controller.admin;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.FtOrder;
import com.ruoyi.system.request.OrderRequest;
import com.ruoyi.system.response.OrderResponse;
import com.ruoyi.system.service.FtOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Desc 订单
 * @Author 方糖
 * @Date 2023-03-30 15:38
 **/
@RestController
@RequestMapping("v1/admin/order")
@Api(tags = "后台-订单")
public class AdminOrderController extends BaseController {

    @Resource
    private FtOrderService orderService;

    @GetMapping("/search")
    @ApiOperation("订单搜索")
    public AjaxResult orderSearch(String phone) {
        return AjaxResult.success(orderService.searchByPhone(phone));
    }

    @GetMapping("/page")
    @ApiOperation("订单列表-分页")
    public TableDataInfo getOrderList(OrderRequest request) {
        startPage();
        List<OrderResponse> responses = orderService.selectOrderList(request);
        return getDataTable(responses);
    }

    @GetMapping("/detail")
    @ApiOperation("订单详情")
    public AjaxResult getOrderDetail(@RequestParam(value = "id") Long id) {
        return AjaxResult.success(orderService.selectByPrimaryKey(id));
    }

    @GetMapping("/couponNum")
    @ApiOperation("套餐卷商品销量")
    public AjaxResult getCouponNum(String str) {
        return AjaxResult.success(orderService.getCouponNum(str));
    }


    @PostMapping("/insert")
    @ApiOperation("新增订单")
    public AjaxResult addOrder(@RequestBody FtOrder order) {
        return AjaxResult.success(orderService.addOrder(order));
    }

    @PostMapping("/update")
    @ApiOperation("修改订单")
    public AjaxResult updateOrder(@RequestBody FtOrder order) {
        return AjaxResult.success(orderService.updateOrder(order));
    }

    @GetMapping("/list")
    @ApiOperation("订单列表")
    public AjaxResult selectOrderList(OrderRequest order) {
        return AjaxResult.success(orderService.selectOrderList(order));
    }

    @DeleteMapping("/remove")
    @ApiOperation("删除订单")
    public AjaxResult delete(@RequestParam Long id) {
        return AjaxResult.success(orderService.deleteByPrimaryKey(id));
    }

    @PostMapping("/pay")
    @ApiOperation("支付订单")
    public AjaxResult payOrder(@RequestParam Long id) {
        return AjaxResult.success(orderService.payOrder(id));
    }

}
