package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserGoods;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2023/5/1 23:30
 */
@Repository
public interface UserGoodsMapper {

    boolean deleteUserGoodsByUserIdAndGoodsId(@Param("userId") Long userId,
                                              @Param("goodsId") Long goodsId);

    void batchUserGoods(List<UserGoods> userGoods);

    List<UserGoods> selectByGoodsId(@Param("goodsId") Long goodsId);

    void insert(UserGoods userGoods);

    List<UserGoods> selectByUserId(Long userId);

    List<UserGoods> getUserGoods();

    void insertBatch(List<UserGoods> userGoods);
}
