package com.ruoyi.system.response;


import com.ruoyi.system.domain.FtOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/31 22:52
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class OrderResponse extends FtOrder {

    @ApiModelProperty(value = "地址")
    private String address;

    private String userName;

    @ApiModelProperty(value = "用户电话")
    private String userPhone;

    private String homeName;

    private String schoolName;

    private String payMethod;

    private String deliveryType;

    private Long schoolId;

    private List<OrderElementsResponse> orderElementsResponses;
}
