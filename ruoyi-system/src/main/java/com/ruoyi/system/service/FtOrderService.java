package com.ruoyi.system.service;

import com.ruoyi.system.entity.FtOrder;
import com.ruoyi.system.request.OrderRequest;
import com.ruoyi.system.response.OrderResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/30 15:59
 */
public interface FtOrderService {

    Boolean deleteByPrimaryKey(Long id);

    Boolean addOrder(FtOrder record);

    FtOrder selectByPrimaryKey(Long id);

    Boolean updateOrder(FtOrder record);

    List<OrderResponse> selectOrderList(OrderRequest order);

    Boolean payOrder(Long id);

    List<OrderResponse> searchByPhone(String phone);

    List<OrderResponse> getCouponNum(String str);
}
