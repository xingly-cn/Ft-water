package com.ruoyi.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/22 23:12
 */
@Data
@ToString
public class PageRequest {

    @ApiModelProperty(value = "页码")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数量")
    private Integer size = 10;
}
