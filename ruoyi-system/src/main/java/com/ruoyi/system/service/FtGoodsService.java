package com.ruoyi.system.service;


import com.ruoyi.system.entity.FtGoods;
import com.ruoyi.system.request.GoodRequest;
import com.ruoyi.system.response.GoodsResponse;

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

    List<GoodsResponse> selectGoodsList(GoodRequest request);

    FtGoods selectByPrimaryKey(Long id);
}
