package com.ruoyi.system.service;

import com.ruoyi.system.domain.FtOrder;
import com.ruoyi.system.request.OrderPayRequest;
import com.ruoyi.system.request.OrderRequest;
import com.ruoyi.system.response.CalcOrderPriceResponse;
import com.ruoyi.system.response.OrderHomeCountResponse;
import com.ruoyi.system.response.OrderResponse;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/30 15:59
 */
public interface FtOrderService {

    Boolean deleteByPrimaryKey(Long id);

    FtOrder selectByPrimaryKey(Long id);

    Boolean updateOrder(OrderRequest request);

    Long addOrder(OrderRequest request);

    List<OrderResponse> selectOrderList(OrderRequest order);

    Boolean payOrder(OrderPayRequest request);

    List<OrderResponse> searchByPhone(String phone);

    List<OrderResponse> getCouponNum(String str);

    String createOrderCQ(String orderId) throws UnsupportedEncodingException;

    String checkOrderCQ(String encBody, String type);

    List<OrderHomeCountResponse> homeCount(Long userId);

    List<CalcOrderPriceResponse> getOrderPrice(Long orderId);

    String getGoodId(String orderId);

    String getUseNum(String orderId);

    String createWxNoCQ(String wxNo) throws UnsupportedEncodingException;
}
