package com.ruoyi.system.service;

import com.ruoyi.system.request.ShopRequest;
import com.ruoyi.system.response.ShopResponse;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/5/2 17:14
 */
public interface ShopService {
    Boolean insertShop(ShopRequest request);

    Boolean updateShop(ShopRequest request);

    Boolean deleteShop(Long id);

    List<ShopResponse> getShopList(ShopRequest request);

    ShopResponse getShopDetail(Long id);
}
