package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.FtSale;
import com.ruoyi.system.request.SaleRequest;
import com.ruoyi.system.response.SaleResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/30 15:59
 */
@Repository
public interface FtSaleMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(FtSale record);

    FtSale selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FtSale record);

    int updateByPrimaryKey(FtSale record);

    List<SaleResponse> selectList(@Param("sale") SaleRequest sale);

    FtSale checkExist(String orderId);
}