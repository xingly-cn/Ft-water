package com.ruoyi.web.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.web.controller.request.GoodRequest;
import com.ruoyi.web.controller.response.GoodsResponse;
import com.ruoyi.web.dao.FtGoodsMapper;
import com.ruoyi.web.entity.FtGoods;
import com.ruoyi.web.service.FtGoodsService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/30 15:59
 */
@Service
public class FtGoodsServiceImpl extends BaseMapperImpl<FtGoods, GoodsResponse, GoodRequest, FtGoodsMapper> implements FtGoodsService {

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
    public Map<String, Object> getGoodsPage(GoodRequest request) {
        IPage<GoodsResponse> page = new Page<>(request.getPage(), request.getSize());
        return selectPage(page, request);
    }

    @Override
    public List<FtGoods> selectGoodsList(FtGoods Goods) {
        return ftGoodsMapper.selectList(new QueryWrapper<>(Goods));
    }

    @Override
    @Cacheable(value = "goods", key = "#id", unless = "#result == null")
    public FtGoods selectByPrimaryKey(Long id) {
        return ftGoodsMapper.selectByPrimaryKey(id);
    }

    @Override
    protected void customSelectPage(IPage<GoodsResponse> page, GoodRequest request) {
        ftGoodsMapper.selectPage(page, request);
    }
}
