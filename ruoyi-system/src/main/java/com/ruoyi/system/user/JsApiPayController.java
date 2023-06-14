package com.ruoyi.system.user;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.JsApiPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Desc
 * @Author 方糖
 * @Date 2023-06-11 21:43
 **/
@RestController
@RequestMapping("v1/wechat/pay")
@Api(tags = "支付模块")
@Slf4j
public class JsApiPayController {

    @Autowired
    private JsApiPayService payService;

    @GetMapping("/createNo")
    @ApiOperation("JSAPI下单")
    public AjaxResult createPay(Long orderId) {
        return AjaxResult.success(payService.createPay(orderId));
    }

    @GetMapping("/refund")
    @ApiOperation("退款")
    public AjaxResult refund(String wxNo) {
        return AjaxResult.success(payService.refund(wxNo));
    }


    @ApiOperation("paySign")
    @GetMapping("wakePay")
    public AjaxResult wakePay(String prepay_id) {
        // 调用老罗的 order/pay 完成支付后的相关操作，如优惠券扣除，发送通知，订单状态变更等
        return AjaxResult.success(payService.wakePay(prepay_id));
    }
}
