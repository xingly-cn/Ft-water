package com.ruoyi.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/31 21:51
 */
@Data
@ToString
public class LoginRequest {

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "wx.login返回的code")
    private String code;

    @ApiModelProperty(value = "头像")
    private String avatar;


    @ApiModelProperty(value = "昵称")
    private String name;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "寝室类型 0:男寝 1:女寝")
    private Integer dormType;

    @ApiModelProperty(value = "楼栋ID")
    private Long homeId;

    @ApiModelProperty(value = "地址")
    private Long addressId;
}
