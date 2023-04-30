package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.FtSale;
import com.ruoyi.system.mapper.FtSaleMapper;
import com.ruoyi.system.request.SaleRequest;
import com.ruoyi.system.response.SaleResponse;
import com.ruoyi.system.service.FtSaleService;
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
    public Boolean deleteByPrimaryKey(Long id) {
        return ftSaleMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public Boolean addSale(FtSale record) {
        record.setCreateTime(new Date());
        return ftSaleMapper.insertSelective(record) > 0;
    }

    @Override
    public FtSale selectByPrimaryKey(Long id) {
        return ftSaleMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean updateSale(FtSale record) {
        return ftSaleMapper.updateByPrimaryKeySelective(record) > 0;
    }

    @Override
    public List<SaleResponse> selectSaleList(SaleRequest sale) {
        return ftSaleMapper.selectList(sale);
    }
}
