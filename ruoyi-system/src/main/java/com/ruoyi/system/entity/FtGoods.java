package com.ruoyi.system.entity;

import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/30 15:59
 */

/**
 * 商品表
 */
@ApiModel(description = "商品表")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FtGoods extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 2179907408868362354L;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String title;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    /**
     * 商品信息（备注）
     */
//    @ApiModelProperty(value = "商品信息（备注）")
//    private String remark;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String avatar;

    @ApiModelProperty(value = "商品类型")
    private Boolean typer;

    @ApiModelProperty(value = "上架状态")
    private Boolean opener;

    @ApiModelProperty(value = "运费")
    private BigDecimal runPrice;

    @ApiModelProperty(value = "赠送水票水量")
    private Integer waterNum;

    @ApiModelProperty(value = "最小购买")
    private Integer minNum;

    @ApiModelProperty(value = "最大购买")
    private Integer maxNum;

    @ApiModelProperty(value = "商品性别显示")
    private Integer goodSex;

    @ApiModelProperty(value = "取货方式")
    private Boolean getType;
}