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
}
