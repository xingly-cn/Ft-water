package com.ruoyi.system.domain;

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
@ApiModel(description = "ft_message")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
public class FtMessage extends BaseEntity {

    private static final long serialVersionUID = 8510398728211060703L;

    /**
     * 楼栋id
     */
    @ApiModelProperty(value = "楼栋id")
    private Long homeId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 是否确认
     */
    @ApiModelProperty(value = "是否确认")
    private Integer confirm;

    private Integer number;

    private Boolean operator;

    @Tolerate
    public FtMessage() {}
}