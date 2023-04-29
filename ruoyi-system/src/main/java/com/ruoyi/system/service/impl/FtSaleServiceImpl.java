package com.ruoyi.system.service.impl;

import com.ruoyi.system.entity.FtSale;
import com.ruoyi.system.mapper.FtSaleMapper;
import com.ruoyi.system.request.SaleRequest;
import com.ruoyi.system.response.SaleResponse;
import com.ruoyi.system.service.FtSaleService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/30 15:59
 */
@Service
public class FtSaleServiceImpl implements FtSaleService {

    @Resource
    private FtSaleMapper ftSaleMapper;

    @Override
    @CacheEvict(value = "sale", key = "#id")
    public Boolean deleteByPrimaryKey(Long id) {
        return ftSaleMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    @CachePut(value = "sale", key = "#record.id")
    public Boolean addSale(FtSale record) {
        record.setCreateTime(new Date());
        return ftSaleMapper.insertSelective(record) > 0;
    }

    @Override
    @Cacheable(value = "sale", key = "#id")
    public FtSale selectByPrimaryKey(Long id) {
        return ftSaleMapper.selectByPrimaryKey(id);
    }

    @Override
    @CachePut(value = "sale", key = "#record.id")
    public Boolean updateSale(FtSale record) {
        return ftSaleMapper.updateByPrimaryKeySelective(record) > 0;
    }

    @Override
    public List<SaleResponse> selectSaleList(SaleRequest sale) {
        return ftSaleMapper.selectList(sale);
    }
}
