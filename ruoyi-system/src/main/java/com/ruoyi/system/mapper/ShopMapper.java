package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.Shop;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2023/5/2 15:31
 */
public interface ShopMapper {

    Shop selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Shop shop);

    int updateByPrimaryKey(Shop shop);

    int insert(Shop shop);

    int insertSelective(Shop shop);

    int deleteByPrimaryKey(Long id);
}
