package com.ruoyi.web.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
* Created by IntelliJ IDEA.
* @Author : 镜像
* @create 2023/3/30 15:59
*/
/**
    * 商品表
    */
@ApiModel(description="商品表")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FtGoods extends BaseEntry implements Serializable {

    private static final long serialVersionUID = 2179907408868362354L;
    /**
    * 商品名称
    */
    @ApiModelProperty(value="商品名称")
    private String title;

    /**
    * 商品价格
    */
    @ApiModelProperty(value="商品价格")
    private BigDecimal price;

    /**
    * 商品信息（备注）
    */
    @ApiModelProperty(value="商品信息（备注）")
    private String remark;

    /**
    * 商品图片
    */
    @ApiModelProperty(value="商品图片")
    private String avatar;
}