package com.ruoyi.web.entity;

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
* @Author : 镜像
* @create 2023/4/19 21:48
*/
@ApiModel(description="ft_message")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
public class FtMessage extends BaseEntry {

    private static final long serialVersionUID = 8510398728211060703L;

    /**
    * 楼栋id
    */
    @ApiModelProperty(value="楼栋id")
    private Long homeId;

    /**
    * 用户id
    */
    @ApiModelProperty(value="用户id")
    private Long userId;

    /**
    * 是否确认
    */
    @ApiModelProperty(value="是否确认")
    private Boolean confirm;

    private Integer number;

    /**
    * 创建人
    */
    @ApiModelProperty(value="创建人")
    private Long createBy;

    /**
    * 修改人
    */
    @ApiModelProperty(value="修改人")
    private Long updateBy;

    /**
    * 创建时间
    */
    @ApiModelProperty(value="创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
    * 修改时间
    */
    @ApiModelProperty(value="修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Tolerate
    public FtMessage() {}
}