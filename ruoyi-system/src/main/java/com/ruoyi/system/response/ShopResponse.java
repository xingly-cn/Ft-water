package com.ruoyi.system.response;

import com.ruoyi.system.domain.Shop;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/5/2 17:16
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class ShopResponse extends Shop {

    private GoodsResponse goodsResponse;
}

