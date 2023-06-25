package com.ruoyi.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @Desc
 * @Author 方糖
 * @Date 2023-06-24 21:40
 **/
@ApiModel(description = "统计表")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FtStatistics  implements Serializable {
    @ApiModelProperty(value = "id")
    private int id;

    @ApiModelProperty(value = "楼层ID")
    private int floorId;

    @ApiModelProperty(value = "商品类型 0水票 1水 2桶")
    private int tp;

    @ApiModelProperty(value = "数量")
    private int total;

    @ApiModelProperty(value = "核销人ID")
    private int userId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
