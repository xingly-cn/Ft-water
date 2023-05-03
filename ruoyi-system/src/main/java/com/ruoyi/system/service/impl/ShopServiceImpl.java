package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.exception.ServiceException;
import com.ruoyi.system.mapper.ShopMapper;
import com.ruoyi.system.request.ShopRequest;
import com.ruoyi.system.response.ShopResponse;
import com.ruoyi.system.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/5/2 17:14
 */
@Service
@Slf4j
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Override
    public Boolean insertShop(ShopRequest request) {
        Long userId = SecurityUtils.getUserId();
        request.setUserId(userId);
        if (request.getGoodsId() == null) {
            throw new ServiceException("商品不能为空");
        }
        //先去查询是否已经存在该商品 存在 累加
        ShopResponse shopResponse = shopMapper.selectByGoodsId(userId,request.getGoodsId());
        if (shopResponse != null) {
            log.info("已经存在该商品，累加数量");
            request.setNumber(shopResponse.getNumber() + request.getNumber());
            return shopMapper.updateByPrimaryKeySelective(request) > 0;
        }
        log.info("不存在该商品，新增");
        return shopMapper.insertSelective(request) > 0;
    }

    @Override
    public Boolean updateShop(ShopRequest request) {
        return shopMapper.updateByPrimaryKeySelective(request) > 0;
    }

    @Override
    public Boolean deleteShop(Long id) {
        return shopMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public List<ShopResponse> getShopList(ShopRequest request) {
        request.setUserId(SecurityUtils.getUserId());
        return shopMapper.selectShopList(request);
    }

    @Override
    public ShopResponse getShopDetail(Long id) {
        return shopMapper.selectByPrimaryKey(id);
    }

    public void deleteShopsByIds(List<Long> ids) {
        shopMapper.deleteShopsByIds(ids);
    }
}
