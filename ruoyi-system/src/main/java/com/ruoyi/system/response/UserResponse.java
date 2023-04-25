package com.ruoyi.system.response;


import com.ruoyi.system.entity.FtUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/31 21:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends FtUser {

    private String token;

    @ApiModelProperty(value = "宿舍类型 男0 女1")
    private String dormTypeName;

    @ApiModelProperty(value = "用户类型：0学生 1宿管 2管理 3  注册默认为学生 拼接")
    private String userTypeName;

    @ApiModelProperty(value = "楼栋ID")
    private String homeName;
}
