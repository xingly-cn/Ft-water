package com.ruoyi.system.domain;

import lombok.Data;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/5/1 23:24
 */
@Data
@ToString
public class UserGoods {

    private Long userId;

    private Long goodsId;

    private Boolean isDeleted;
}
