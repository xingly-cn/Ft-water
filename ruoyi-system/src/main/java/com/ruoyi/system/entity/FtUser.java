package com.ruoyi.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
 * 分销商-宿管-学生表
 */
@ApiModel(description = "分销商-宿管-学生表")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class FtUser extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -8451607199884042434L;
    /**
     * 微信唯一ID
     */
    @ApiModelProperty(value = "微信唯一ID")
    private String openId;

    @ApiModelProperty(value = "昵称")
    private String name;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "密码")
    private String password;


    @ApiModelProperty(value = "名称缩写")
    private String smallName;

    @ApiModelProperty(value = "宿舍类型 男0 女1")
    private Integer dormType;

    /**
     * 微信获取手机号
     */
    @ApiModelProperty(value = "微信获取手机号")
    private String phone;

    /**
     * 用户类型：0学生 1宿管
     */
    @ApiModelProperty(value = "用户类型：0学生 1宿管 2管理 3  注册默认为学生 拼接")
    private String userType;

    /**
     * 所属学校
     */
    @ApiModelProperty(value = "地址")
    private String address;

    /**
     * 剩余水量
     */
    @ApiModelProperty(value = "剩余水量")
    private Integer waterNum;

    @ApiModelProperty(value = "楼栋ID")
    private String homeId;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
}