package com.ruoyi.web.service;


import com.ruoyi.web.controller.request.GoodRequest;
import com.ruoyi.web.entity.FtGoods;

import java.util.List;
import java.util.Map;

/**
* Created by IntelliJ IDEA.
* @Author : 镜像
* @create 2023/3/30 15:59
*/
public interface FtGoodsService{

    Boolean deleteByPrimaryKey(Long id);

    Boolean addGoods(FtGoods record);

    Boolean updateGoods(FtGoods record);

    Map<String, Object> getGoodsPage(GoodRequest request);

    List<FtGoods> selectGoodsList(FtGoods Goods);

    FtGoods selectByPrimaryKey(Long id);
}
