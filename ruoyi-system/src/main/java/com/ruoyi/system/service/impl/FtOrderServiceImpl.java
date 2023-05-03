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
import java.util.Set;
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

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Boolean deleteByPrimaryKey(Long id) {
        return ftOrderMapper.deleteByPrimaryKey(id) > 0;
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
    @Transactional(rollbackFor = ServiceException.class)
    public Long addOrder(OrderRequest request) {
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
        List<Long> shopIds = request.getShops().stream().map(Shop::getId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(shopIds)) {
            shopService.deleteShopsByIds(shopIds);
        }
        orderElementsMapper.insertBatch(orderElements);
        return request.getId();
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
        //todo 水漂 单独走 水和空
        FtOrder ftOrder = selectByPrimaryKey(id);
        if (ftOrder == null) {
            throw new ServiceException("订单不存在");
        }
        ftOrder.setStatus(1);
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
        Set<Long> shopGoodsIds = shops.stream().map(Shop::getGoodsId).collect(Collectors.toSet());
        List<GoodsResponse> goods = goodsService.selectGoodsByIds(shopGoodsIds);
        Map<Integer, List<GoodsResponse>> goodsMap = goods.stream().collect(Collectors.groupingBy(GoodsResponse::getTyper));
        goodsMap.keySet().forEach(type -> {
            List<GoodsResponse> goodsResponses = goodsMap.get(type);
            List<Shop> shopList = shops.stream().filter(s -> goodsResponses.stream().map(GoodsResponse::getId).collect(Collectors.toList())
                    .contains(s.getGoodsId())).collect(Collectors.toList());
            //总数量
            int number = shopList.stream().mapToInt(Shop::getNumber).sum();
            switch (type) {
                case 0:
                    //水漂
                    log.info("水漂 - number:{}", number);
                    ftOrder.setStatus(2);
                    //async 水漂 其他 是 核销之后 入这个表
                    addUserGoods(shopList, user.getUserId());
                    break;
                case 1:
                    //水
                    //发送消息 给对应宿管
                    log.info("水 - number:{}", number);
                    homeService.sendMessageAndNotices(homeId, user.getUserId(), false, homeResponse.getNumber(), number, false);
                    break;
                case 2:
                    //桶
                    //发送消息 给对应宿管
                    log.info("桶 - number:{}", number);
                    homeService.sendMessageAndNotices(homeId, user.getUserId(), false, 0, number, true);
                    break;
                default:
                    break;
            }
        });

        ftOrder.setPayed(true);
        OrderRequest orderRequest = new OrderRequest();
        BeanUtils.copyProperties(ftOrder, orderRequest);
        //支付之后
        //todo 收货的时候需要根据type=3的商品来增加用户的空桶数量
//        userMapper.updateWaterNumberById(user.getUserId(), number);
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
                        .number(shop.getNumber())
                        .build());
            });
            userGoodsMapper.insertBatch(userGoods);
        }
    }
}
