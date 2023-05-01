package com.ruoyi.system.response;


import com.ruoyi.system.domain.FtOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/31 22:52
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class OrderResponse extends FtOrder {

    @ApiModelProperty(value = "商品名称")
    private String goodName;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户电话")
    private String userPhone;

    @ApiModelProperty(value = "总价")
    private BigDecimal total;
}
