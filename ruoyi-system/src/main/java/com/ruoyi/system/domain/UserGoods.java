package com.ruoyi.system.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/5/1 23:24
 */
@Data
@ToString
@Builder
@AllArgsConstructor
public class UserGoods {

    private Long userId;

    private Long goodsId;

    private Integer number;

    private Boolean isDeleted;

    @Tolerate
    public UserGoods() {}
}
