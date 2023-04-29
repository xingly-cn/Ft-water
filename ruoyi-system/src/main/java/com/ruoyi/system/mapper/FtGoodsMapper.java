package com.ruoyi.system.mapper;


import com.ruoyi.system.entity.FtGoods;
import com.ruoyi.system.request.GoodRequest;
import com.ruoyi.system.response.GoodsResponse;
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
public interface FtGoodsMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(FtGoods record);

    FtGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FtGoods record);

    int updateByPrimaryKey(FtGoods record);

    List<GoodsResponse> selectList(@Param("goods") GoodRequest request);
}