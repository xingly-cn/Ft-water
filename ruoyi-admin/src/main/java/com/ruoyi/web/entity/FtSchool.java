package com.ruoyi.web.entity;

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
 * 学校表
 */
@ApiModel(description = "学校表")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FtSchool extends BaseEntry implements Serializable {

    private static final long serialVersionUID = -9196141504722593355L;
    /**
     * 学校名称
     */
    @ApiModelProperty(value = "学校名称")
    private String schoolName;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private Double longitude;

    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private Double latitude;

    /**
     * 备用字段
     */
    @ApiModelProperty(value = "备用字段")
    private String remark;

}