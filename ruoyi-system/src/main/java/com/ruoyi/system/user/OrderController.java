package com.ruoyi.system.user;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.request.OrderRequest;
import com.ruoyi.system.response.OrderResponse;
import com.ruoyi.system.service.FtOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Desc 订单
 * @Author 方糖
 * @Date 2023-03-30 15:36
 **/
@RestController
@RequestMapping("v1/wechat/user/order")
@Api(tags = "小程序-订单")
public class OrderController extends BaseController {

    @Autowired
    private FtOrderService orderService;

    @GetMapping("/page")
    @ApiOperation("订单列表")
    public TableDataInfo getOrderList(OrderRequest request) {
        startPage();
        request.setFlag(true);
        List<OrderResponse> responses = orderService.selectOrderList(request);
        return getDataTable(responses);
    }

    @PostMapping("/shopCart")
    @ApiOperation("加入购物车")
    public AjaxResult shopCart(@RequestBody OrderRequest request) {
        request.setFlag(true);
        return AjaxResult.success(orderService.shopCart(request));
    }

    @PostMapping("/update")
    @ApiOperation("修改订单")
    public AjaxResult updateOrder(@RequestBody OrderRequest request) {
        return AjaxResult.success(orderService.updateOrder(request));
    }

    @PostMapping("/add")
    @ApiOperation("下单")
    public AjaxResult addOrder(@RequestBody OrderRequest request) {
        return AjaxResult.success(orderService.addOrder(request));
    }

    @GetMapping("/list")
    @ApiOperation("订单列表")
    public AjaxResult selectOrderList(OrderRequest order) {
        order.setFlag(false);
        return AjaxResult.success(orderService.selectOrderList(order));
    }

//    @DeleteMapping("/remove")
//    @ApiOperation("删除订单")
    public AjaxResult delete(Long id) {
        return AjaxResult.success(orderService.deleteByPrimaryKey(id));
    }

    @PostMapping("/pay")
    @ApiOperation("支付订单")
    public AjaxResult payOrder(Long id) {
        return AjaxResult.success(orderService.payOrder(id));
    }

    @GetMapping("/detail")
    @ApiOperation("订单详情")
    public AjaxResult getOrderDetail(Long id) {
        return AjaxResult.success(orderService.selectByPrimaryKey(id));
    }
}
