package com.ruoyi.web.controller.admin;

import com.alibaba.fastjson.JSONObject;
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
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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

    @GetMapping("createCQ")
    @ApiOperation("生成二维码")
    public AjaxResult createOrderCQ(String orderId, HttpServletRequest request) throws UnsupportedEncodingException {
        String header = request.getHeader("version");
        if (header == null) {
            return AjaxResult.error("非法访问, 已记录IP");
        }
        int index = Integer.parseInt(header.substring(0, 1));
        String[] dict = {"DF", "WR", "OF", "VB", "PQ", "EX", "UM"};
        ConcurrentHashMap<String, Object> res = new ConcurrentHashMap<>(2);
        int goodId = Integer.parseInt(orderService.getGoodId(orderId));
        int useNum = Integer.parseInt(orderService.getUseNum(orderId));
        res.put("encBody", orderService.createOrderCQ(orderId));
        res.put("getCode", dict[index - 1] + "-" + Integer.toHexString(Integer.parseInt(orderId))  + "-" + Integer.toHexString(goodId) + "-" + Integer.toHexString(useNum));
        return AjaxResult.success(res);
    }

    @GetMapping("createWxNoCQ")
    @ApiOperation("生成二维码")
    public AjaxResult createWxNoCQ(String wxNo, HttpServletRequest request) throws UnsupportedEncodingException {
        String header = request.getHeader("version");
        if (header == null) {
            return AjaxResult.error("非法访问, 已记录IP");
        }
        int index = Integer.parseInt(header.substring(0, 1));
        String[] dict = {"DF", "WR", "OF", "VB", "PQ", "EX", "UM"};
        ConcurrentHashMap<String, Object> res = new ConcurrentHashMap<>(2);
        res.put("encBody", orderService.createWxNoCQ(wxNo));
//        res.put("getCode", dict[index - 1] + "-" + String.);
        return AjaxResult.success(res);
    }

    @GetMapping("/checkCQ")
    @ApiOperation("二维码核销")
    public AjaxResult checkOrderCQ(String encBody, String type) {
        String s = orderService.checkOrderCQ(encBody, type);
        String[] split = s.split("&");
        if (split.length == 1) {
            return AjaxResult.success(split[0]);
        }
        JSONObject res = new JSONObject();
        res.put("msg", split[0]);
        res.put("useNum", split[1]);
        return AjaxResult.success(res);
    }

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
