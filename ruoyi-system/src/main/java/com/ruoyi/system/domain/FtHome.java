package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/18 21:04
 */
@ApiModel(description = "树表")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FtHome extends BaseEntity {

    private static final long serialVersionUID = 7802128654790973740L;

    @ApiModelProperty(value = "父级id")
    private Long parentId;

    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "库存")
    private Integer number;

    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    private Double latitude;

    @ApiModelProperty(value = "是否配送")
    private Boolean isDelivery;
}
