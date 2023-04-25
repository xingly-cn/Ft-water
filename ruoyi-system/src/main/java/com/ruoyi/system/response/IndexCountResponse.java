package com.ruoyi.system.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/31 22:42
 */
@Data
@ToString
@ApiModel(value = "首页统计数据")
@Builder
public class IndexCountResponse {

    @ApiModelProperty(value = "学校id")
    private Long schoolId;

    @ApiModelProperty(value = "学校")
    private String schoolName;

    private Integer count;

    private String time;

    @Tolerate
    public IndexCountResponse() {}
}
