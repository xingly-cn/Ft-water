package com.ruoyi.system.domain;

import lombok.Data;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/5/2 15:32
 */
@Data
@ToString
public class Shop {

    private Long id;

    private Long userId;

    private Long goodsId;

    private Integer number;

    private Boolean isDeleted;
}
