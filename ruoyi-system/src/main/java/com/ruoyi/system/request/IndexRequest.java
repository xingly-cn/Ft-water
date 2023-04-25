package com.ruoyi.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/31 22:47
 */
@Data
@ToString
public class IndexRequest {

    @ApiModelProperty(value = "0:按天统计 1:按月统计")
    private Integer choose;

    @ApiModelProperty(value = "学校id")
    private List<Long> schoolIds;
}
