package com.ruoyi.system.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/6/25 17:56
 */
@Data
@ToString
public class CountResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer number;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long homeId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal price;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long userId;
}
