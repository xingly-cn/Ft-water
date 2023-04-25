package com.ruoyi.web.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Desc
 * @Author 方糖
 * @Date 2023-04-01 15:11
 **/
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserRequest extends PageRequest {

    private String name;

    private String type;

    private String phone;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "新密码")
    private String newPassword;

    @ApiModelProperty(value = "验证码")
    private String code;

    private String address;
}
