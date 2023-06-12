package com.ruoyi.system.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.crypto.SecureUtil;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.exception.ServiceException;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.request.OrderRequest;
import com.ruoyi.system.response.*;
import com.ruoyi.system.service.FtOrderService;
import com.ruoyi.system.utils.DateUtils;
import com.ruoyi.system.utils.WechatUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
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

    @Resource
    private FtSaleMapper ftSaleMapper;

    @Resource
    private FtHomeMapper ftHomeMapper;

    @Resource
    private FtGoodsMapper ftGoodsMapper;

    @Autowired
    private SysConfigServiceImpl configService;

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
        if (CollectionUtils.isNotEmpty(shopIds) && StringUtils.isNotEmpty(request.getPayType()) && !request.getPayType().equals("goods")) {
            log.info("delete shop :{}", request.getShops());
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
        //订单的homeId
        Long homeId = ftOrder.getHomeId();
        List<Shop> shops = Lists.newArrayList();
        elements.forEach(element -> {
            shops.add(Shop.builder()
                    .goodsId(element.getGoodsId())
                    .number(element.getNumber())
                    .build());
        });

        if (homeId == null) {
            throw new ServiceException("宿舍不存在");
        }
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
                    homeService.sendMessageAndNotices(homeId, user.getUserId(), false, homeResponse.getNumber(), number, false,1);
                    //消息订阅
                    Map<String, Object> data = new HashMap<>();
                    //订单编号
                    data.put("character_string2", new HashMap<String, String>() {{
                        put("value", String.valueOf(ftOrder.getId())); // 替换为具体值
                    }});
                    data.put("thing11", new HashMap<String, String>() {{
                        put("value", "水"); // 替换为具体值
                    }});
                    //支付金额
                    data.put("amount1", new HashMap<String, String>() {{
                        put("value", "支付金额"); // 替换为具体值
                    }});
                    //支付时间
                    data.put("date3", new HashMap<String, String>() {{
                        put("value", DateUtils.getCurrentDate()); // 替换为具体值
                    }});
                    //提货码
                    data.put("character_string9", new HashMap<String, String>() {{
                        put("value", ""); // 替换为具体值
                    }});
                    WechatUtil.sendSubscriptionMessage(user.getOpenId(),"3",data);
                    break;
                case 2:
                    //桶
                    //发送消息 给对应宿管
                    log.info("桶 - number:{}", number);
                    homeService.sendMessageAndNotices(homeId, user.getUserId(), false, 0, number, true,2);
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

    @Override
    public String createOrderCQ(String orderId) {
        Long userId = SecurityUtils.getUserId();

        FtOrder ftOrder = ftOrderMapper.selectByPrimaryKey(Long.valueOf(orderId));
        if (!ftOrder.getPayed()) {
            return "订单未支付";
        }

        if (!Objects.equals(ftOrder.getUserId(), userId)) {
            return "订单不属于当前用户, 非法操作已记录 ";
        }

        CQ orderCQ = ftOrderMapper.createOrderCQ(orderId);
        // 生成二维码
        String body = orderCQ.getId() + "_" + orderCQ.getUserId() + "_" + orderCQ.getHomeId() + "_" + orderCQ.getNumber() + "_" + orderCQ.getGoodId()  + "_" + LocalDateTimeUtil.now();
        String encBody = SecureUtil.aes("aEsva0zDHECg47P8SuPzmw==".getBytes()).encryptBase64(body);
        return "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + encBody;
    }

    @Override
    public String checkOrderCQ(String encBody) {
        // 解密数据
        String s = SecureUtil.aes("aEsva0zDHECg47P8SuPzmw==".getBytes()).decryptStr(encBody);
        String[] split = s.split("_");

        Long operatorId = SecurityUtils.getUserId();    // 操作员ID
        long usedNum = Long.parseLong(split[3]);    // 核销数量

        // 核销校验-是否已核销
        FtSale f = ftSaleMapper.checkExist(split[0]);
        if (f != null) {
            return "该订单已核销, 非法操作已记录, 用户ID：" + split[0];
        }

        // 获取核销类型
        FtGoods ftGoods = ftGoodsMapper.selectByPrimaryKey(Long.parseLong(split[4]));
        Integer typer = ftGoods.getTyper();

        //--------------------水商品 和 空桶商品 核销--------------------
        SysUser user = userService.selectUserById(Long.parseLong(split[1]));

        switch (typer) {
            case 0:
                return "该订单为水票券, 不需要核销, 订单ID：" + split[0];
            case 1: // 水核销
                // 当前楼库存查询
                FtHome ftHome = ftHomeMapper.selectByPrimaryKey(Long.parseLong(split[2]));
                Integer LocalNumber = ftHome.getNumber();

                if (usedNum > LocalNumber) {
                    return "核销失败, 当前楼栋剩余水数量不足, 楼栋ID：" + split[2] +  ", 剩余数量：" + LocalNumber + ", 核销数量：" + usedNum;
                }

                // 插入核销表
                FtSale ftSale = new FtSale(split[1], Integer.parseInt(split[0]), operatorId.toString());
                ftSaleMapper.insertSelective(ftSale);

                // 插入user_goods表
                UserGoods userGoods = new UserGoods(Long.parseLong(split[1]), Long.parseLong(split[0]), Integer.parseInt(split[3]), false);
                userGoodsMapper.insert(userGoods);

                // 当前楼栋水库存更新
                ftHome.setNumber(LocalNumber - Integer.parseInt(split[3]));
                ftHomeMapper.updateByPrimaryKeySelective(ftHome);

                // 订单状态更新
                FtOrder ftOrder = ftOrderMapper.selectByPrimaryKey(Long.parseLong(split[0]));
                ftOrder.setStatus(2);
                ftOrderMapper.updateByPrimaryKey(ftOrder);

                //消息订阅
                Map<String, Object> data = new HashMap<>();
                data.put("thing3", new HashMap<String, String>() {{
                    put("value", "水"); // 替换为具体值
                }});
                data.put("thing7", new HashMap<String, String>() {{
                    put("value", "订单核销"); // 替换为具体值
                }});

                //提货地址
//                String name = homes.stream().filter(h -> h.getId().equals(homeId)).findFirst().orElse(new FtHome()).getName();
//                String topName = homes.stream().filter(h -> h.getId().equals(topId)).findFirst().orElse(new FtHome()).getName();
                data.put("thing9", new HashMap<String, String>() {{
                    put("value", "核销穿进来的地址"); // 替换为具体值
                }});

                //说明
                data.put("thing1", new HashMap<String, String>() {{
                    put("value", "宿管进行订单核销"); // 替换为具体值
                }});

                data.put("phone_number6", new HashMap<String, String>() {{
                    put("value", configService.getCacheValue("manage_phone")); // 替换为具体值
                }});
                WechatUtil.sendSubscriptionMessage(user.getOpenId(),"4",data);

                return "核销水成功, 用户ID：" + split[1] + ", 数量：" + split[3] + ", 订单ID：" + split[0];
            case 2: // 空桶核销
                // 插入核销表
                FtSale ftSale1 = new FtSale(split[1], Integer.parseInt(split[0]), operatorId.toString());
                ftSaleMapper.insertSelective(ftSale1);

                // 插入user_goods表
                UserGoods userGoods1 = new UserGoods(Long.parseLong(split[1]), Long.parseLong(split[0]), Integer.parseInt(split[3]), false);
                userGoodsMapper.insert(userGoods1);

                // 当前用户空桶数量更新
                Integer waterNum = user.getWaterNum();
                user.setWaterNum(waterNum + Integer.parseInt(split[3]));
                userService.updateUser(user);

                // 订单状态更新
                FtOrder ftOrder1 = ftOrderMapper.selectByPrimaryKey(Long.parseLong(split[0]));
                ftOrder1.setStatus(2);
                ftOrderMapper.updateByPrimaryKey(ftOrder1);

                return "核销空桶成功, 用户ID：" + split[1] + ", 数量：" + split[3] + ", 订单ID：" + split[0];
        }

        return "暂无核销类型";
    }

    @Override
    public CalcOrderPriceResponse getOrderPrice(Long orderId) {
        return ftOrderMapper.getOrderPrice(orderId);
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
