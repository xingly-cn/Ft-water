package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

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
     * 学校ID
     */
    @ApiModelProperty(value = "地址ID")
    private Long addressId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    private Long homeId;

    @ApiModelProperty(value = "0待付款 1代取货 2已取货")
    private Integer status;

    /**
     * 支付状态
     */
    @ApiModelProperty(value = "支付状态")
    private Boolean payed;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<OrderElements> orderElements;
}