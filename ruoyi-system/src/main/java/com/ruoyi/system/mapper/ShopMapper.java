package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.Shop;
import com.ruoyi.system.request.ShopRequest;
import com.ruoyi.system.response.ShopResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2023/5/2 15:31
 */
public interface ShopMapper {

    ShopResponse selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Shop shop);

    int updateByPrimaryKey(Shop shop);

    int insert(Shop shop);

    int insertSelective(Shop shop);

    int deleteByPrimaryKey(Long id);

    List<ShopResponse> selectShopList(ShopRequest request);

    void deleteShopsByIds(@Param("ids") List<Long> ids);
}
