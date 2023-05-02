package com.ruoyi.system.response;

import com.ruoyi.system.domain.OrderElements;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/5/2 14:55
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class OrderElementsResponse extends OrderElements {

    private String goodName;

    private BigDecimal price;
}
