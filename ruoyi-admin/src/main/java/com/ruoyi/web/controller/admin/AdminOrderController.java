package com.ruoyi.web.controller.admin;

import com.ruoyi.system.request.OrderRequest;
import com.ruoyi.system.entity.FtOrder;
import com.ruoyi.system.service.FtOrderService;
import com.ruoyi.system.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Desc 订单
 * @Author 方糖
 * @Date 2023-03-30 15:38
 **/
@RestController
@RequestMapping("v1/admin/order")
@Api(tags = "后台-订单")
public class AdminOrderController {

    @Resource
    private FtOrderService orderService;

    @GetMapping("/search")
    @ApiOperation("订单搜索")
    public Result<?> OrderSearch (String phone) {
        return Result.success(orderService.searchByPhone(phone));
    }

    @GetMapping("/page")
    @ApiOperation("订单列表-分页")
    public Result<?> getOrderList(OrderRequest request) {
        return Result.success(orderService.getOrderPage(request));
    }

    @GetMapping("/detail")
    @ApiOperation("订单详情")
    public Result<?> getOrderDetail(@RequestParam(value = "id") Long id) {
        return Result.success(orderService.selectByPrimaryKey(id));
    }

    @GetMapping("/couponNum")
    @ApiOperation("套餐卷商品销量")
    public Result<?> getCouponNum(String str) {
        return Result.success(orderService.getCouponNum(str));
    }

//    @GetMapping("/goodNum")       // 缺订单表
//    @ApiOperation("水商品销量")
//    public Result<?> getGoodsNum(GoodRequest goodRequest) {
//        return Result.success(goodsService.getGoodsPage(goodRequest));
//    }

    @PostMapping("/insert")
    @ApiOperation("新增订单")
    public Result<?> addOrder(@RequestBody FtOrder order) {
        return Result.success(orderService.addOrder(order));
    }

    @PostMapping("/update")
    @ApiOperation("修改订单")
    public Result<?> updateOrder(@RequestBody FtOrder order) {
        return Result.success(orderService.updateOrder(order));
    }

    @GetMapping("/list")
    @ApiOperation("订单列表")
    public Result<?> selectOrderList(OrderRequest order) {
        return Result.success(orderService.selectOrderList(order));
    }

    @DeleteMapping("/remove")
    @ApiOperation("删除订单")
    public Result<?> delete(@RequestParam Long id) {
        return Result.success(orderService.deleteByPrimaryKey(id));
    }

    @PostMapping("/pay")
    @ApiOperation("支付订单")
    public Result<?> payOrder(@RequestParam Long id) {
        return Result.success(orderService.payOrder(id));
    }

}
