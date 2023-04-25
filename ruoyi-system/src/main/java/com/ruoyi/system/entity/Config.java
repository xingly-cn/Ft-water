package com.ruoyi.system.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Config  extends BaseEntry implements Serializable {

    private static final long serialVersionUID = -9054051587705579988L;

    @ApiModelProperty(value="配置key")
    private String configKey;

    @ApiModelProperty(value="配置value")
    private String configValue;

    @ApiModelProperty(value="备注")
    private String remark;

}