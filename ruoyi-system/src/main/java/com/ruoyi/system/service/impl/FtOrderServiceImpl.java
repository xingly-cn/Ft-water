package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.FtOrder;
import com.ruoyi.system.domain.OrderElements;
import com.ruoyi.system.domain.Shop;
import com.ruoyi.system.domain.UserGoods;
import com.ruoyi.system.exception.ServiceException;
import com.ruoyi.system.mapper.FtOrderMapper;
import com.ruoyi.system.mapper.OrderElementsMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.mapper.UserGoodsMapper;
import com.ruoyi.system.request.OrderRequest;
import com.ruoyi.system.response.GoodsResponse;
import com.ruoyi.system.response.HomeResponse;
import com.ruoyi.system.response.OrderResponse;
import com.ruoyi.system.service.FtOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private UserGoodsMapper userGoodsMapper;

    @Autowired
    private OrderElementsMapper orderElementsMapper;

    @Autowired
    private ShopServiceImpl shopService;

    @Autowired
    private SysUserMapper userMapper;

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
        Long userId = SecurityUtils.getUserId();
        SysUser user = userService.selectUserById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        if (CollectionUtils.isEmpty(request.getShops())) {
            throw new ServiceException("商品不能为空");
        }
        HomeResponse homeResponse = homeService.selectByPrimaryKey(user.getHomeId());
        if (homeResponse == null) {
            throw new ServiceException("宿舍不存在");
        }
        //校验库存
        checkGoodsNumber(request.getShops(), homeResponse.getNumber());
        //生成订单
        request.setUserId(userId);
        ftOrderMapper.insertSelective(request);
        List<OrderElements> orderElements = Lists.newArrayList();
        request.getShops().forEach(shop -> {
            orderElements.add(OrderElements.builder()
                    .orderId(request.getId())
                    .goodsId(shop.getGoodsId())
                    .number(shop.getNumber())
                    .build());
        });
        //下单之后删除购物车里面的东西
        shopService.deleteShopsByIds(request.getShops().stream().map(Shop::getId).collect(Collectors.toList()));
        return orderElementsMapper.insertBatch(orderElements);
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
        //todo 收货的时候需要根据type=3的商品来增加用户的空桶数量
        log.info("really pay order");
        FtOrder ftOrder = selectByPrimaryKey(id);
        if (ftOrder == null) {
            throw new ServiceException("订单不存在");
        }
        //需要校验库存
        List<OrderElements> elements = orderElementsMapper.selectElementsByOrderId(id);
        if (CollectionUtils.isEmpty(elements)) {
            throw new ServiceException("订单不存在");
        }
        SysUser user = SecurityUtils.getLoginUser().getUser();
        Long homeId = user.getHomeId();
        List<Shop> shops = Lists.newArrayList();
        elements.forEach(element -> {
            shops.add(Shop.builder()
                    .goodsId(element.getGoodsId())
                    .number(element.getNumber())
                    .build());
        });
        HomeResponse homeResponse = homeService.selectByPrimaryKey(homeId);
        checkGoodsNumber(shops, homeResponse.getNumber());
        //找到对应的商品 - typer->1
        List<Long> shopGoodsIds = shops.stream().map(Shop::getGoodsId).collect(Collectors.toList());
        List<Long> goodsIds =  goodsService.selectGoodsByIdsAndType(shopGoodsIds, 1);
        int number = 0;
        if (CollectionUtils.isNotEmpty(goodsIds)) {
            List<Shop> waterShops = shops.stream().filter(s -> goodsIds.contains(s.getGoodsId())).collect(Collectors.toList());
            number = waterShops.stream().mapToInt(Shop::getNumber).sum();
        }
        ftOrder.setPayed(true);
        ftOrder.setStatus(1);
        //发送消息 给对应宿管
        homeService.sendMessageAndNotices(homeId,user.getId(),false,homeResponse.getNumber(),number);
        //什么用户购买什么商品多少吧
        OrderRequest orderRequest = new OrderRequest();
        BeanUtils.copyProperties(ftOrder, orderRequest);
        updateOrder(orderRequest);
        //async
        addUserGoods(shops, user.getUserId());
        //更新用户的水票
        return userMapper.updateWaterNumberById(user.getId(), number);
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

    private void checkGoodsNumber(List<Shop> shops, Integer number) {

        //库存-该宿舍-楼
        Map<Long, Integer> goodsMap = shops.stream().collect(Collectors.toMap(Shop::getGoodsId, Shop::getNumber));
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

    @Async
    public void addUserGoods(List<Shop> shops, Long userId) {
        //添加用户商品
        if (CollectionUtils.isNotEmpty(shops)) {
            List<UserGoods> userGoods = Lists.newArrayList();
            shops.forEach(shop -> {
                userGoods.add(UserGoods.builder()
                        .goodsId(shop.getGoodsId())
                        .userId(userId)
                        .build());
            });
            userGoodsMapper.insertBatch(userGoods);
        }
    }
}
