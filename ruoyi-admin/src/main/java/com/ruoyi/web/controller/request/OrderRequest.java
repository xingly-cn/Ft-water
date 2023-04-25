package com.ruoyi.web.controller.request;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.web.entity.FtOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/31 23:01
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class OrderRequest extends FtOrder {

    @ApiModelProperty(value = "通用模糊查询")
    private String keyword;

    @ApiModelProperty(value = "页码")
    @TableField(exist = false)
    private Integer page = 1;

    @ApiModelProperty(value = "每页数量")
    @TableField(exist = false)
    private Integer size = 10;
}
