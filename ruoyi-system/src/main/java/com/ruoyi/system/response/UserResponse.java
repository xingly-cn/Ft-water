package com.ruoyi.system.response;


import com.ruoyi.common.core.domain.entity.SysUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/31 21:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends SysUser {

    private String token;

    @ApiModelProperty(value = "宿舍类型 男0 女1")
    private String dormTypeName;

    @ApiModelProperty(value = "楼栋ID")
    private String homeName;

    private String address;

    public String getDormTypeName() {
        if (StringUtils.isNotEmpty(dormTypeName)) {
            return dormTypeName.equals("0") ? "男" : "女";
        }
        return dormTypeName;
    }
}
