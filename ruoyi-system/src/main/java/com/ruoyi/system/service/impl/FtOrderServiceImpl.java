package com.ruoyi.system.service.impl;

import com.ruoyi.system.entity.FtOrder;
import com.ruoyi.system.mapper.FtOrderMapper;
import com.ruoyi.system.request.OrderRequest;
import com.ruoyi.system.response.OrderResponse;
import com.ruoyi.system.service.FtOrderService;
import com.ruoyi.system.websocket.OrderWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class FtOrderServiceImpl implements FtOrderService {

    @Resource
    private FtOrderMapper ftOrderMapper;

    @Autowired
    private OrderWebSocket orderWebSocket;

    @Autowired
    private SysUserServiceImpl userService;

    @Autowired
    private FtGoodsServiceImpl goodsService;

    @Autowired
    private FtHomeServiceImpl homeService;

    @Override
    @CacheEvict(value = "order", key = "#id")
    public Boolean deleteByPrimaryKey(Long id) {
        return ftOrderMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    @CachePut(value = "order", key = "#record.id")
    public Boolean addOrder(FtOrder record) {
        record.setCreateTime(new Date());
        return ftOrderMapper.insertSelective(record) > 0;
    }

    @Override
    @Cacheable(value = "order", key = "#id")
    public FtOrder selectByPrimaryKey(Long id) {
        return ftOrderMapper.selectByPrimaryKey(id);
    }

    @Override
    @CachePut(value = "order", key = "#record.id")
    public Boolean updateOrder(FtOrder record) {
        return ftOrderMapper.updateByPrimaryKeySelective(record) > 0;
    }

    @Override
    public List<OrderResponse> selectOrderList(OrderRequest order) {
        return ftOrderMapper.selectList(order);
    }

    @Override
    public Boolean payOrder(Long id) {
        log.info("really pay order");
        FtOrder ftOrder = selectByPrimaryKey(id);
        if (ftOrder == null) {
            throw new RuntimeException("订单不存在");
        }
        ftOrder.setPayed(true);
        //发送消息
        //什么用户购买什么商品多少吧
        String userName = userService.selectUserById(ftOrder.getUid()).getUserName();
        String goodsName = goodsService.selectByPrimaryKey(ftOrder.getGoodId()).getTitle();
        String schoolName = homeService.selectByPrimaryKey(ftOrder.getSchoolId()).getName();
        orderWebSocket.sendMessage(userName + "在" + schoolName + "购买了" + goodsName + ftOrder.getNum() + "个");
        return updateOrder(ftOrder);
    }

    @Override
    public List<OrderResponse> searchByPhone(String phone) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setKeyword(phone);
        return ftOrderMapper.selectList(orderRequest);
    }

    @Override
    public List<OrderResponse> getCouponNum(String str) {
        OrderRequest orderRequest = new OrderRequest();
        if (str.indexOf(0) >= 'a' && str.indexOf(0) <= 'z') {
            String name = homeService.getSchoolByRemark(str);
            orderRequest.setKeyword(name);
            return ftOrderMapper.selectList(orderRequest);
        }
        orderRequest.setKeyword(str);
        return ftOrderMapper.selectList(orderRequest);
    }
}
