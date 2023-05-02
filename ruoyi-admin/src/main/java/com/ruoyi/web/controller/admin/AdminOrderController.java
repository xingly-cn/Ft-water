package com.ruoyi.web.controller.admin;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.request.OrderRequest;
import com.ruoyi.system.response.OrderResponse;
import com.ruoyi.system.service.FtOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Desc 订单
 * @Author 方糖
 * @Date 2023-03-30 15:38
 **/
@RestController
@RequestMapping("v1/admin/order")
@Api(tags = "订单")
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
        request.setFlag(false);
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
}
