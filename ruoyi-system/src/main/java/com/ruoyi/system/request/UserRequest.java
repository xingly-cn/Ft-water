package com.ruoyi.system.request;

import com.ruoyi.common.core.domain.entity.SysUser;
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
public class UserRequest extends SysUser {

    @ApiModelProperty(value = "新密码")
    private String newPassword;

    @ApiModelProperty(value = "验证码")
    private String code;

}
