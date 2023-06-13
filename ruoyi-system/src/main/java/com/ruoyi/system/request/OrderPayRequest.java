package com.ruoyi.system.request;

import lombok.Data;
import lombok.ToString;

/**
 * @Desc
 * @Author 方糖
 * @Date 2023-06-13 11:21
 **/
@Data
@ToString
public class OrderPayRequest {

    private Long orderId;

    private String wxNo;
}
