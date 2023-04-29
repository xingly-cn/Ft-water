package com.ruoyi.system.entity;

import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Tolerate;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/19 21:48
 */
@ApiModel(description = "ft_notices")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
public class FtNotices extends BaseEntity {

    private static final long serialVersionUID = 5011762148279740830L;

    /**
     * 0 取货 1是收货
     */
    @ApiModelProperty(value = "0 送货 1是收货")
    private Integer type;

    /**
     * 学校id
     */
    @ApiModelProperty(value = "学校id")
    private Long schoolId;

    /**
     * 楼栋id
     */
    @ApiModelProperty(value = "楼栋id")
    private Long homeId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer number;

    /**
     * 剩余
     */
    @ApiModelProperty(value = "剩余")
    private Integer residue;


    @Tolerate
    public FtNotices() {}
}