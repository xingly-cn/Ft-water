package com.ruoyi.system.service;

import com.ruoyi.system.domain.FtOrder;
import com.ruoyi.system.request.OrderRequest;
import com.ruoyi.system.response.OrderResponse;

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

    Boolean payOrder(Long id);

    List<OrderResponse> searchByPhone(String phone);

    List<OrderResponse> getCouponNum(String str);

    String createOrderCQ(String orderId);

    String checkOrderCQ(String encBody);
}
