package com.ruoyi.system.service.impl;


import com.ruoyi.system.entity.FtGoods;
import com.ruoyi.system.mapper.FtGoodsMapper;
import com.ruoyi.system.request.GoodRequest;
import com.ruoyi.system.response.GoodsResponse;
import com.ruoyi.system.service.FtGoodsService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/30 15:59
 */
@Service
public class FtGoodsServiceImpl implements FtGoodsService {

    @Resource
    private FtGoodsMapper ftGoodsMapper;

    @Override
    @CacheEvict(value = "goods", key = "#id")
    public Boolean deleteByPrimaryKey(Long id) {
        return ftGoodsMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    @CachePut(value = "goods", key = "#record.id")
    public Boolean addGoods(FtGoods record) {
        return ftGoodsMapper.insertSelective(record) > 0;
    }


    @Override
    @CachePut(value = "goods", key = "#record.id")
    public Boolean updateGoods(FtGoods record) {
        return ftGoodsMapper.updateByPrimaryKeySelective(record) > 0;
    }

    @Override
    public List<GoodsResponse> selectGoodsList(GoodRequest request) {
        return ftGoodsMapper.selectList(request);
    }

    @Override
    @Cacheable(value = "goods", key = "#id", unless = "#result == null")
    public FtGoods selectByPrimaryKey(Long id) {
        return ftGoodsMapper.selectByPrimaryKey(id);
    }
}
