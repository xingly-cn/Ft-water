package com.ruoyi.system.request;

import com.ruoyi.system.domain.FtSale;
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
public class SaleRequest extends FtSale {

    private String keyword;
}
