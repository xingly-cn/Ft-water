package com.ruoyi.system.request;

import com.ruoyi.system.domain.FtOrder;
import com.ruoyi.system.domain.Shop;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

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

    @ApiModelProperty(value = "是否是小程序")
    private Boolean flag;

    private List<Shop> shops;

    private String payType;

    private String startTime;

    private String endTime;

    private Integer typer;
}
