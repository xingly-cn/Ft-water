package com.ruoyi.web.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* Created by IntelliJ IDEA.
* @Author : 镜像
* @create 2023/3/30 15:59
*/
/**
    * 订单表
    */
@ApiModel(description="订单表")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FtOrder extends BaseEntry implements Serializable {

    private static final long serialVersionUID = 8951707551796931522L;
    /**
    * 学生ID
    */
    @ApiModelProperty(value="学生ID")
    private Long uid;

    /**
    * 商品ID
    */
    @ApiModelProperty(value="商品ID")
    private Long goodId;

    /**
    * 学校ID
    */
    @ApiModelProperty(value="学校ID")
    private Long schoolId;

    /**
    * 购买数量
    */
    @ApiModelProperty(value="购买数量")
    private Integer num;

    /**
    * 购买时间
    */
    @ApiModelProperty(value="购买时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
    * 支付状态
    */
    @ApiModelProperty(value="支付状态")
    private Boolean payed;
}