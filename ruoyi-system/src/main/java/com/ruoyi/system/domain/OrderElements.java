package com.ruoyi.system.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/5/2 14:41
 */
@Data
@ToString
@Builder
public class OrderElements {

    private Long orderId;

    private Long goodsId;

    private Integer number;

    private String payMethod;

    @Tolerate
    public OrderElements() {}
}
