package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.FtOrder;
import com.ruoyi.system.exception.ServiceException;
import com.ruoyi.system.mapper.FtOrderMapper;
import com.ruoyi.system.mapper.UserGoodsMapper;
import com.ruoyi.system.request.OrderRequest;
import com.ruoyi.system.response.GoodsResponse;
import com.ruoyi.system.response.HomeResponse;
import com.ruoyi.system.response.OrderResponse;
import com.ruoyi.system.service.FtOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private SysUserServiceImpl userService;

    @Autowired
    private FtGoodsServiceImpl goodsService;

    @Autowired
    private FtHomeServiceImpl homeService;

    @Autowired
    private FtNoticesServiceImpl noticesService;

    @Autowired
    private FtMessageServiceImpl messageService;

    @Autowired
    private UserGoodsMapper userGoodsMapper;

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Boolean deleteByPrimaryKey(Long id) {
        return ftOrderMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Boolean shopCart(OrderRequest request) {
        request.setCreateTime(new Date());
        if (request.getFlag().equals(true)) {
            //小程序端
            request.setUserId(SecurityUtils.getUserId());
        }
        return ftOrderMapper.insertSelective(request) > 0;
    }

    @Override
    public FtOrder selectByPrimaryKey(Long id) {
        return ftOrderMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Boolean updateOrder(OrderRequest request) {
        return ftOrderMapper.updateByPrimaryKeySelective(request) > 0;
    }

    @Override
    public Boolean addOrder(OrderRequest request) {
        //批量下单  一个订单对应多个商品
        Long userId = SecurityUtils.getUserId();
        SysUser user = userService.selectUserById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        if (CollectionUtils.isEmpty(request.getOrders())) {
            throw new ServiceException("商品不能为空");
        }
        //校验库存
        checkGoodsNumber(request.getOrders(), user.getHomeId());
        //进行下单
        //status 0待付款 1代取货 2已取货  (-1 ->0)
        //补充地址 有id
        return null;
    }

    @Override
    public List<OrderResponse> selectOrderList(OrderRequest order) {
        if (order.getFlag().equals(true)) {
            //小程序端
            order.setUserId(SecurityUtils.getUserId());
        }
        return ftOrderMapper.selectList(order);
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Boolean payOrder(Long id) {
        log.info("really pay order");
        FtOrder ftOrder = selectByPrimaryKey(id);
        if (ftOrder == null) {
            throw new ServiceException("订单不存在");
        }
        ftOrder.setPayed(true);
        //需要校验库存

        //发送消息 给对应宿管
        //什么用户购买什么商品多少吧
        OrderRequest orderRequest = new OrderRequest();
        BeanUtils.copyProperties(ftOrder, orderRequest);
        return updateOrder(orderRequest);
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

    private void checkGoodsNumber(List<OrderRequest> requests, Long homeId) {
        HomeResponse homeResponse = homeService.selectByPrimaryKey(homeId);
        if (homeResponse == null) {
            throw new ServiceException("宿舍不存在");
        }
        //库存-该宿舍-楼
        Integer number = homeResponse.getNumber();
//        Map<Long, Integer> goodsMap = requests.stream().collect(Collectors.toMap(OrderRequest::getGoodId, OrderRequest::getNum));
        Map<Long, Integer> goodsMap = null;
        List<GoodsResponse> goodsResponses = goodsService.selectGoodsByIds(goodsMap.keySet());
        //校验商品是否存在
        if (CollectionUtils.isEmpty(goodsResponses)) {
            throw new ServiceException("商品不存在");
        }
        for (GoodsResponse goodsResponse : goodsResponses) {
            //校验商品是否下架 opener
            if (goodsResponse.getOpener().equals(false)) {
                throw new ServiceException(goodsResponse.getTitle() + "已下架，请重新选择");
            }
            if (goodsResponse.getTyper().equals(1)) {
                //水需要校验库存
                Integer num = goodsMap.get(goodsResponse.getId());
                if (num > number) {
                    log.info("current number is {},has number is {}", number, num);
                    throw new ServiceException(goodsResponse.getTitle() + "，库存不足");
                }
            }
        }
    }
}
