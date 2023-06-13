package com.ruoyi.system.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.crypto.SecureUtil;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.exception.ServiceException;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.request.OrderPayRequest;
import com.ruoyi.system.request.OrderRequest;
import com.ruoyi.system.response.*;
import com.ruoyi.system.service.FtOrderService;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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
    private FtHomeMapper homeMapper;

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

    @Autowired
    private UserHomeMapper userHomeMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private FtMessageServiceImpl messageService;

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

        if (request.getDeliveryType().equals("selfGet")) {
            //自提
            if (request.getHomeId() == null) {
                throw new ServiceException("自提地址不能为空");
            }
            List<UserHome> userHomes = userHomeMapper.selectByHomeId(request.getHomeId());
            if (CollectionUtils.isEmpty(userHomes)) {
                throw new ServiceException("自提地址不存在管理员");
            }
            request.setManageUserId(StringUtils.join(userHomes.stream().map(UserHome::getUserId).collect(Collectors.toList()), ","));
        } else if (request.getDeliveryType().equals("goDoor")) {
            //配送
            if (request.getAddressId() == null) {
                throw new ServiceException("配送地址不能为空");
            }
            //todo address
            Address address = addressMapper.selectByPrimaryKey(request.getAddressId());
            if (address == null) {
                throw new ServiceException("配送地址不存在");
            }
            List<UserHome> userHomes = userHomeMapper.selectByHomeId(address.getHomeId());
            if (CollectionUtils.isEmpty(userHomes)) {
                throw new ServiceException("配送地址不存在管理员");
            }
            request.setManageUserId(StringUtils.join(userHomes.stream().map(UserHome::getUserId).collect(Collectors.toList()), ","));
        }

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
            order.setManageUserId(SecurityUtils.getUserIdStr());
        }
        List<OrderResponse> responses = ftOrderMapper.selectList(order);
        if (CollectionUtils.isNotEmpty(responses)) {
            List<FtHome> homes = homeService.getHomes();
            responses.forEach(response -> {
                if (response.getDeliveryType().equals("selfGet")) {
                    //自提
                    String name = homes.stream().filter(f -> f.getId().equals(response.getHomeId())).findFirst().orElse(new FtHome()).getName();
                    response.setHomeName(homeService.getTopHome(homes, response.getHomeId()).getName() + "/" + name);
                }
            });
        }
        return responses;
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Boolean payOrder(OrderPayRequest request) {
        log.info("really pay order:" + request);

        // 查询用户
        Long userId = SecurityUtils.getUserId();
        SysUser user = userService.selectUserById(userId);
//        log.info("user ->" + user);


        // 查出订单信息
        FtOrder ftOrder = selectByPrimaryKey(request.getOrderId());
        if (ftOrder == null) {
            throw new ServiceException("订单不存在");
        }

        // 设置订单状态为已支付
        ftOrder.setStatus(1);

        //需要校验库存
        List<OrderElements> elements = orderElementsMapper.selectElementsByOrderId(request.getOrderId());
        if (CollectionUtils.isEmpty(elements)) {
            throw new ServiceException("订单不存在");
        }

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
                    // 套餐 = 水票
                    log.info("套餐 - number:{}", number);
                    ftOrder.setStatus(2);
                    //async 水漂 其他 是 核销之后 入这个表
                    addUserGoods(shopList, user.getUserId());
                    AtomicInteger waterNum = new AtomicInteger();
                    goodsResponses.forEach(goodsResponse -> {
                        //校验
                        if (number > goodsResponse.getMinNum() && number < goodsResponse.getMaxNum()) {
                            log.info("套餐赠送水票 - number:{}", goodsResponse.getWaterNum());
                            waterNum.set(goodsResponse.getWaterNum());
                        }
                    });
                    // 增加用户水票
                    user.setWaterNum(user.getWaterNum() + waterNum.get());
                    userService.updateUser(user);
                    break;
                case 1:
                    //商品 = 水

                    // 如果为混合支付扣除用户水票
                    if ("coupon".equals(ftOrder.getPayMethod())) {
                        log.info("用户水票为：" + user.getWaterNum() + " 购买水数：" + number);
                        user.setWaterNum(user.getWaterNum() - number);
                    } else if ("mixed".equals(ftOrder.getPayMethod())) {
                        user.setWaterNum(0);
                    }
                    userService.updateUser(user);

                    //发送消息 给对应宿管
                    log.info("水 - number:{}", number);
                    homeService.sendMessageAndNotices(homeId, user.getUserId(), false, homeResponse.getNumber(), number, false, 1);
                    //消息订阅
                    Map<String, Object> data = new HashMap<>();

                    //订单编号
                    data.put("character_string2", new HashMap<String, String>() {{
                        put("value", String.valueOf(ftOrder.getId())); // 订单id
                    }});
                    data.put("thing11", new HashMap<String, String>() {{
                        put("value", "水"); // 商品名称
                    }});
                    //支付金额
                    data.put("amount1", new HashMap<String, String>() {{
                        put("value", String.valueOf(ftOrder.getTotal())); // 支付金额
                    }});
                    //支付时间
                    String formatTime = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
                    data.put("date3", new HashMap<String, String>() {{
                        put("value", formatTime); // 替换为具体值
                    }});
                    //提货码
                    data.put("character_string9", new HashMap<String, String>() {{
                        put("value", "小程序查看提货码"); // 提货码
                    }});
                    WechatUtil.sendSubscriptionMessage(user.getOpenId(), "3", data);
                    break;
                case 2:
                    // 空桶
                    //发送消息 给对应宿管
                    log.info("桶 - number:{}", number);
                    // 增加用户空桶
                    user.setBarrelNumber(user.getBarrelNumber() + number);
                    userService.updateUser(user);
                    homeService.sendMessageAndNotices(homeId, user.getUserId(), false, 0, number, true, 2);
                    break;
                default:
                    break;
            }
        });

        ftOrder.setPayed(true);
        ftOrder.setWxno(request.getWxNo());
        OrderRequest orderRequest = new OrderRequest();
        BeanUtils.copyProperties(ftOrder, orderRequest);
        //支付之后

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
    public String createOrderCQ(String orderId) throws UnsupportedEncodingException {
        Long userId = SecurityUtils.getUserId();

        FtOrder ftOrder = ftOrderMapper.selectByPrimaryKey(Long.valueOf(orderId));
        if (!ftOrder.getPayed()) {
            return "订单未支付";
        }

        if (!Objects.equals(ftOrder.getUserId(), userId)) {
            return "订单不属于当前用户, 非法操作已记录 ";
        }

        OrderResponse response = ftOrderMapper.createOrderCQ(orderId);
        //orderCQ.getHomeId() 俩种
        Long homeId = response.getHomeId();
        if (response.getDeliveryType().equals("goDoor")) {
            //配送
            //todo address
            Address address = addressMapper.selectByPrimaryKey(response.getAddressId());
            homeId = address.getHomeId();
        }

        // 生成二维码
        String body = response.getId() + "_" + response.getUserId() + "_" + homeId + "_" + response.getNumber() + "_" + response.getGoodId() + "_" + LocalDateTimeUtil.now();
        String encBody = SecureUtil.aes("aEsva0zDHECg47P8SuPzmw==".getBytes()).encryptBase64(body);
        log.info("encBody:{}", encBody);
        return "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + URLEncoder.encode(encBody, "UTF-8");
    }

    @Override
    public String checkOrderCQ(String encBody, String type) {
        // 判断核销类型 二维码/提货码
        Integer typer = null;
        Long operatorId = SecurityUtils.getUserId();    // 操作员ID
        Integer usedNum = null;
        String userId = null;   //ok
        String orderId = null;  //ok
        String homeId = null;     // ok


        if ("code".equals(type)) {
            String[] split = encBody.split("-");
            orderId = String.valueOf(Integer.parseInt(split[1], 16));
            int goodId = Integer.parseInt(split[2], 16);
            FtOrder ftOrder = ftOrderMapper.selectByPrimaryKey(Long.valueOf(orderId));
            userId = ftOrder.getUserId().toString();
            homeId = ftOrder.getHomeId().toString();
            usedNum = Integer.parseInt(split[3], 16);

            // 核销校验-是否已核销
            FtSale f = ftSaleMapper.checkExist(orderId);
            if (f != null) {
                return "该订单已核销, 非法操作已记录, 用户ID：" + userId;
            }

            // 获取核销类型
            FtGoods ftGoods = ftGoodsMapper.selectByPrimaryKey((long) goodId);
            typer = ftGoods.getTyper();

        } else if ("scan".equals(type)) {
            String s = SecureUtil.aes("aEsva0zDHECg47P8SuPzmw==".getBytes()).decryptStr(encBody);
            String[] split = s.split("_");
            userId = split[1];
            orderId = split[0];
            homeId = split[2];
            usedNum = Integer.parseInt(split[3]);

            // 核销校验-是否已核销
            FtSale f = ftSaleMapper.checkExist(orderId);
            if (f != null) {
                return "该订单已核销, 非法操作已记录, 用户ID：" + userId;
            }

            // 获取核销类型
            FtGoods ftGoods = ftGoodsMapper.selectByPrimaryKey(Long.parseLong(split[4]));
            typer = ftGoods.getTyper();
        }

        String result = null;

        //--------------------水商品 和 空桶商品 核销--------------------
        SysUser user = userService.selectUserById(Long.parseLong(userId));
        FtHome ftHome = ftHomeMapper.selectByPrimaryKey(Long.parseLong(homeId));

        switch (typer) {
            case 0:
                return "该订单为水票券, 不需要核销, 订单ID：" + orderId;
            case 1: // 水核销
                // 当前楼库存查询
                Integer LocalNumber = ftHome.getNumber();

                if (usedNum > LocalNumber) {
                    return "核销失败, 当前楼栋剩余水数量不足, 楼栋ID：" + homeId + ", 剩余数量：" + LocalNumber + ", 核销数量：" + usedNum;
                }

                // 插入核销表
                FtSale ftSale = new FtSale(userId, Integer.parseInt(orderId), operatorId.toString());
                ftSaleMapper.insertSelective(ftSale);

                // 插入user_goods表
                UserGoods userGoods = new UserGoods(Long.parseLong(userId), Long.parseLong(orderId), usedNum, false);
                userGoodsMapper.insert(userGoods);

                // 当前楼栋水库存更新
                ftHome.setNumber(LocalNumber - usedNum);
                ftHomeMapper.updateByPrimaryKeySelective(ftHome);

                // 订单状态更新
                FtOrder ftOrder = ftOrderMapper.selectByPrimaryKey(Long.parseLong(orderId));
                ftOrder.setStatus(2);
                ftOrderMapper.updateByPrimaryKey(ftOrder);

                result = "核销水成功, 用户ID：" + usedNum + ", 数量：" + usedNum + ", 订单ID：" + orderId + "&" + usedNum;
                break;
            case 2:
                // 空桶核销
                // 插入核销表
                FtSale ftSale1 = new FtSale(userId, Integer.parseInt(orderId), operatorId.toString());
                ftSaleMapper.insertSelective(ftSale1);

                // 插入user_goods表
                UserGoods userGoods1 = new UserGoods(Long.parseLong(userId), Long.parseLong(orderId), Integer.parseInt(homeId), false);
                userGoodsMapper.insert(userGoods1);

                // 当前用户空桶数量更新
                Integer waterNum = user.getWaterNum();
                user.setWaterNum(waterNum + Integer.parseInt(homeId));
                userService.updateUser(user);

                // 订单状态更新
                FtOrder ftOrder1 = ftOrderMapper.selectByPrimaryKey(Long.parseLong(orderId));
                ftOrder1.setStatus(2);
                ftOrderMapper.updateByPrimaryKey(ftOrder1);

                result = "核销空桶成功, 用户ID：" + userId + ", 数量：" + usedNum + ", 订单ID：" + orderId + "&" + usedNum;
                break;
        }

        if (typer == 1 || typer == 2) {
            List<FtHome> homes = homeService.getHomes();
            String topName = homeService.getTopHome(homes, Long.valueOf(homeId)).getName();
            //消息订阅
            Map<String, Object> data = new HashMap<>();

            Integer finalTyper = typer;
            data.put("thing3", new HashMap<String, String>() {{
                put("value", finalTyper == 1 ? "水" : "空桶");
            }});

            data.put("thing7", new HashMap<String, String>() {{
                put("value", "您的订单已送达");
            }});

            data.put("thing9", new HashMap<String, String>() {{
                put("value", topName + "/" + ftHome.getName());
            }});

            //说明
            data.put("thing1", new HashMap<String, String>() {{
                put("value", "交易成功");
            }});

            data.put("phone_number6", new HashMap<String, String>() {{
                put("value", configService.getCacheValue("manage_phone")); // 管理员电话
            }});
            WechatUtil.sendSubscriptionMessage(user.getOpenId(), "4", data);
        }

        return result;
    }

    @Override
    public List<OrderHomeCountResponse> homeCount(Long userId) {
        //找到素有的楼栋
        List<FtOrder> orders = ftOrderMapper.homeCount(userId);
        if (CollectionUtils.isEmpty(orders)) {
            return Collections.emptyList();
        }
        List<OrderHomeCountResponse> responses = new ArrayList<>();

        List<FtHome> homes = homeService.getHomes();
        Set<Long> addressIds = orders.stream()
                .filter(o -> o.getDeliveryType().equals("goDoor"))
                .map(FtOrder::getAddressId).collect(Collectors.toSet());
        List<Address> addresses = CollectionUtils.isEmpty(addressIds) ? Collections.emptyList() : addressMapper.selectByIds(addressIds);

        Set<Long> homeIds = orders.stream()
                .filter(o -> o.getDeliveryType().equals("selfGet"))
                .map(FtOrder::getHomeId).collect(Collectors.toSet());
        homeIds.addAll(addresses.stream().map(Address::getHomeId).collect(Collectors.toSet()));
        //待入库的水的数量 栋楼的水的数量
        List<FtHome> waterCountList = homeMapper.waterCount(homeIds);
        Map<Long, Integer> waterCountMap = waterCountList.stream().collect(Collectors.toMap(FtHome::getId, FtHome::getNumber));
        List<FtMessage> waterWaiteCountList = messageService.waterWaiteCount(userId);
        Map<Long, Integer> waterWaiteCountMap = waterWaiteCountList.stream().collect(Collectors.toMap(FtMessage::getHomeId, FtMessage::getNumber));

        Map<String, List<FtOrder>> orderMap = orders.stream().collect(Collectors.groupingBy(FtOrder::getDeliveryType));
        orderMap.forEach((k, v) -> {
            if (k.equals("selfGet")) {
                //自提
                Map<Long, List<FtOrder>> orderHomeMap = v.stream().collect(Collectors.groupingBy(FtOrder::getHomeId));
                orderHomeMap.forEach((ok, ov) -> {
                    assembleHomeCountResponse(responses, homes, ov, ok, waterCountMap, waterWaiteCountMap);
                });

            } else if (k.equals("goDoor")) {
                //配送
                Map<Long, List<FtOrder>> orderAddressMap = v.stream().collect(Collectors.groupingBy(FtOrder::getAddressId));
                orderAddressMap.forEach((ok, ov) -> {
                    if (CollectionUtils.isEmpty(addresses)) {
                        return;
                    }
                    Long homeId = addresses.stream().filter(h -> h.getId().equals(ok)).findFirst().orElse(new Address()).getHomeId();

                    assembleHomeCountResponse(responses, homes, ov, homeId, waterCountMap, waterWaiteCountMap);
                });
            }
        });
        return responses;
    }

    private void assembleHomeCountResponse(List<OrderHomeCountResponse> responses,
                                           List<FtHome> homes,
                                           List<FtOrder> ov,
                                           Long homeId,
                                           Map<Long, Integer> waterCountMap,
                                           Map<Long, Integer> waterWaiteCountMap) {
        if (CollectionUtils.isNotEmpty(responses)) {
            if (responses.stream().anyMatch(r -> r.getHomeId().equals(homeId))) {
                responses.stream()
                        .filter(r -> r.getHomeId().equals(homeId))
                        .findFirst().ifPresent(r -> {
                            r.setCount(r.getCount() + (int) ov.stream().filter(o -> o.getStatus().equals(2)).count());
                            r.setWaitCount(r.getWaitCount() + (int) ov.stream().filter(o -> o.getStatus().equals(1)).count());
                        });
                return;
            }
        }

        String name = homes.stream().filter(h -> h.getId().equals(homeId)).findFirst().orElse(new FtHome()).getName();
        Long topId = homeService.getTopId(homes, homeId);
        String topName = homes.stream().filter(h -> h.getId().equals(topId)).findFirst().orElse(new FtHome()).getName();
        responses.add(OrderHomeCountResponse.builder()
                .homeId(homeId)
                .homeName(topName + "/" + name)
                .waitCount((int) ov.stream().filter(o -> o.getStatus().equals(1)).count())
                .count((int) ov.stream().filter(o -> o.getStatus().equals(2)).count())
                .waterCount(waterCountMap.get(homeId) != null ? waterCountMap.get(homeId) : 0)
                .waterWaiteCount(waterWaiteCountMap.get(homeId) != null ? waterWaiteCountMap.get(homeId) : 0)
                .build());
    }

    @Override
    public CalcOrderPriceResponse getOrderPrice(Long orderId) {
        return ftOrderMapper.getOrderPrice(orderId);
    }

    @Override
    public String getGoodId(String orderId) {
        return ftOrderMapper.getGoodId(orderId);
    }

    @Override
    public String getUseNum(String orderId) {
        return ftOrderMapper.getUseNum(orderId);
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
