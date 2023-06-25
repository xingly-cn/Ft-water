package com.ruoyi.system.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/6/25 23:12
 */
@Data
@ToString
@Builder
public class SaleCountResponse {

    private Integer orderNum;

    private Long homeId;

    private String homeName;

    private Long userId;

    private String userName;

    private Integer couponNum;

    private Integer waterNum;

    private Integer bottomNum;

    private BigDecimal totalPrice;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SaleCountResponse> users;

    @Tolerate
    public SaleCountResponse() {}
}
