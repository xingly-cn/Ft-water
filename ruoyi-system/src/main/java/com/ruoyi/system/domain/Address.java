package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/19 22:03
 */
@ApiModel(value = "address")
@Data
@EqualsAndHashCode(callSuper = true)
public class Address extends BaseEntity {

    private static final long serialVersionUID = 2856422720749910494L;

    @ApiModelProperty(value = "用户唯一标识")
    @Excel(name = "用户唯一标识", width = 35)
    private Long userId;

    @ApiModelProperty(value = "手机号")
    @Excel(name = "手机号")
    private String phone;

    @ApiModelProperty(value = "省市区")
    @Excel(name = "省市区")
    private String province;

    @ApiModelProperty(value = "详细地址")
    @Excel(name = "详细地址")
    private String address;

    @ApiModelProperty(value = "是否是默认地址")
    private Boolean isDefault;

    @ApiModelProperty(value = "是否删除")
    private Boolean isDel;
}