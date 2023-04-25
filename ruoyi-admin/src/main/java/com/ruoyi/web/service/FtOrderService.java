package com.ruoyi.web.service;


import com.ruoyi.web.controller.request.OrderRequest;
import com.ruoyi.web.controller.response.OrderResponse;
import com.ruoyi.web.entity.FtOrder;

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

    Map<String, Object> getOrderPage(OrderRequest request);

    List<OrderResponse> selectOrderList(OrderRequest order);

    Boolean payOrder(Long id);
}
