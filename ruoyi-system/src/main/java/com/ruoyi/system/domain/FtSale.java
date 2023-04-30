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
 * 核销表
 */
@ApiModel(description = "核销表")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FtSale extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 8165095717770965540L;
    /**
     * 学生ID
     */
    @ApiModelProperty(value = "学生ID")
    private String uid;

    /**
     * 核销数量
     */
    @ApiModelProperty(value = "核销数量")
    private Integer num;

    /**
     * 核销人员ID
     */
    @ApiModelProperty(value = "核销人员ID")
    private String operateId;
}