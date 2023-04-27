package com.ruoyi.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.system.entity.FtGoods;
import com.ruoyi.system.request.GoodRequest;
import com.ruoyi.system.response.GoodsResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
* Created by IntelliJ IDEA.
* @Author : 镜像
* @create 2023/3/30 15:59
*/
@Repository
public interface FtGoodsMapper extends BaseMapper<FtGoods> {

    int deleteByPrimaryKey(Long id);

    int insertSelective(FtGoods record);

    FtGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FtGoods record);

    int updateByPrimaryKey(FtGoods record);

    Page<GoodsResponse> selectPage(@Param("page") IPage<GoodsResponse> page,
                                   @Param("goods") GoodRequest request);
}