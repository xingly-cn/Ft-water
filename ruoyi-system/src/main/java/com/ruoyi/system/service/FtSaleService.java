package com.ruoyi.system.service;


import com.ruoyi.system.entity.FtSale;
import com.ruoyi.system.request.SaleRequest;
import com.ruoyi.system.response.SaleResponse;

import java.util.List;
import java.util.Map;

/**
* Created by IntelliJ IDEA.
* @Author : 镜像
* @create 2023/3/30 15:59
*/
public interface FtSaleService{

    Boolean deleteByPrimaryKey(Long id);

    Boolean addSale(FtSale record);

    FtSale selectByPrimaryKey(Long id);

    Boolean updateSale(FtSale record);

    List<SaleResponse> selectSaleList(SaleRequest Sale);
}
