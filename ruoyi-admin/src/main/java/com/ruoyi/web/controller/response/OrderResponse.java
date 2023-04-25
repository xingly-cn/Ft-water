package com.ruoyi.web.controller.response;


import com.ruoyi.web.entity.FtOrder;
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

    @ApiModelProperty(value = "学校名称")
    private String schoolName;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户电话")
    private String userPhone;

    @ApiModelProperty(value = "总价")
    private BigDecimal total;
}
