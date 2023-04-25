package com.ruoyi.web.service;


import com.ruoyi.web.controller.request.SaleRequest;
import com.ruoyi.web.controller.response.SaleResponse;
import com.ruoyi.web.entity.FtSale;

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

    Map<String,Object> getSalePage(SaleRequest request);

    List<SaleResponse> selectSaleList(SaleRequest Sale);
}
