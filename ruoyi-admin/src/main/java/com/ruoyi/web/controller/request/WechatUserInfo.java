package com.ruoyi.web.controller.request;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/18 21:58
 */
@Data
@ToString
public class WechatUserInfo {

    @ApiModelProperty(value="小程序唯一标识")
    private String openId;

    @ApiModelProperty(value = "昵称")
    private String name;

    @ApiModelProperty(value = "楼栋ID")
    private String homeId;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "宿舍类型 男0 女1")
    @TableField("dormType")
    private Integer dormType;

    @ApiModelProperty(value = "地址")
    private String address;
}
