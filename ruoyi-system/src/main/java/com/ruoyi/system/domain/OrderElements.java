package com.ruoyi.system.domain;

import lombok.Data;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/5/2 14:41
 */
@Data
@ToString
public class OrderElements {

    private Long orderId;

    private Long goodsId;

    private Integer number;
}
