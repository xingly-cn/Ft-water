package com.ruoyi.system.response;


import com.ruoyi.system.entity.FtSale;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Desc
 * @Author 方糖
 * @Date 2023-04-01 22:16
 **/
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class SaleResponse extends FtSale {
    @ApiModelProperty(value = "被核销人姓名")
    private String userName;

    @ApiModelProperty(value = "核销人姓名")
    private String operatorName;

}
