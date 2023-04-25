package com.ruoyi.system.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Desc
 * @Author 方糖
 * @Date 2023-04-01 15:35
 **/
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class SaleRequest extends PageRequest {

    private String keyword;
}
