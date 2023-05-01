package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/30 15:59
 */

/**
 * 订单表
 */
@ApiModel(description = "订单表")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FtOrder extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 8951707551796931522L;
    /**
     * 学生ID
     */
    @ApiModelProperty(value = "学生ID")
    private Long uid;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long goodId;

    /**
     * 学校ID
     */
    @ApiModelProperty(value = "地址ID")
    private Long addressId;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer num;

    @ApiModelProperty(value = " -1 购物车 0待付款 1代取货 2已取货' ")
    private Integer status;

    /**
     * 支付状态
     */
    @ApiModelProperty(value = "支付状态")
    private Boolean payed;
}