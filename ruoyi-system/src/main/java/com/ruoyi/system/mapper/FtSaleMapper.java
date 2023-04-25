package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.web.controller.request.SaleRequest;
import com.ruoyi.web.controller.response.SaleResponse;
import com.ruoyi.system.entity.FtSale;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* Created by IntelliJ IDEA.
* @Author : 镜像
* @create 2023/3/30 15:59
*/
@Repository
public interface FtSaleMapper extends BaseMapper<FtSale> {

    int deleteByPrimaryKey(Long id);

    int insertSelective(FtSale record);

    FtSale selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FtSale record);

    int updateByPrimaryKey(FtSale record);

    Page<SaleResponse> selectPage(@Param("page") IPage<SaleResponse> page,
                                  @Param("sale") SaleRequest request);

    List<SaleResponse> selectList(@Param("sale") SaleRequest sale);
}