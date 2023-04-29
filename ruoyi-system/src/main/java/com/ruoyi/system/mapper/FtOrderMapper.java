package com.ruoyi.system.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.system.entity.FtOrder;
import com.ruoyi.system.request.OrderRequest;
import com.ruoyi.system.response.OrderResponse;
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
public interface FtOrderMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(FtOrder record);

    FtOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FtOrder record);

    int updateByPrimaryKey(FtOrder record);

    Page<OrderResponse> selectPage(@Param("page") IPage<OrderResponse> page,
                                   @Param("order") OrderRequest request);

    List<OrderResponse> selectList(@Param("order") OrderRequest order);
}