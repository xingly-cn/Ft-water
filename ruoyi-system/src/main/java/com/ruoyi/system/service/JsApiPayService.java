package com.ruoyi.system.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/6/14 11:01
 */
public interface JsApiPayService {

    JSONObject createPay(Long orderId);

    JSONObject refund(String wxNo);

    Map<String,String> wakePay(String prepayId);
}
