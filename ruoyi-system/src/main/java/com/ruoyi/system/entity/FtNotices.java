package com.ruoyi.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Tolerate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

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
public class FtNotices extends BaseEntry {

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


    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createBy;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private Long updateBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Tolerate
    public FtNotices() {}
}