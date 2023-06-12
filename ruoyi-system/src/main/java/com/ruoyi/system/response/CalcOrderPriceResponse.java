package com.ruoyi.system.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Desc
 * @Author 方糖
 * @Date 2023-06-12 22:55
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CalcOrderPriceResponse {
    private String deliveryType;
    private String payMethod;
    private Long userId;
    private Integer buyNum;
    private BigDecimal price;
    private BigDecimal runPrice;
    private Integer waterNum;
}
