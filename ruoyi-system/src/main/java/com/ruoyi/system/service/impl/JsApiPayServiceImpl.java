package com.ruoyi.system.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.FtOrder;
import com.ruoyi.system.mapper.FtOrderMapper;
import com.ruoyi.system.response.CalcOrderPriceResponse;
import com.ruoyi.system.response.OrderResponse;
import com.ruoyi.system.service.FtOrderService;
import com.ruoyi.system.service.JsApiPayService;
import com.ruoyi.system.utils.WxUtils;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import lombok.SneakyThrows;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/6/14 11:01
 */
@Service
public class JsApiPayServiceImpl implements JsApiPayService {

    private static final Logger logger = LoggerFactory.getLogger(JsApiPayServiceImpl.class);

    @Autowired
    private FtOrderService ftOrderService;

    @Autowired
    private FtOrderMapper orderMapper;

    @Autowired
    private SysConfigServiceImpl configService;

    @Autowired
    private SysUserServiceImpl userService;


    @Override
    @SneakyThrows
    @Transactional
    public JSONObject createPay(Long orderId) {
        String openId = SecurityUtils.getOpenId();

        // 自动获取微信证书, 第一次获取证书绕过鉴权
        CloseableHttpClient httpClient = buildHttpClient();

        String wechatAppId = configService.getCacheValue("wechat_appid");
        String merchantId = configService.getCacheValue("merchantId");

        // 构建订单数据
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        // 计算价格
        logger.info("订单数据 -> 开始");

//        CalcOrderPriceResponse orderPrice = ftOrderService.getOrderPrice(orderId);
        List<CalcOrderPriceResponse> orderPrices = ftOrderService.getOrderPrice(orderId);
        logger.info("订单数据 {}", JSON.toJSONString(orderPrices));

        AtomicInteger price = new AtomicInteger();

        Set<String> types = orderPrices.stream().map(CalcOrderPriceResponse::getDeliveryType).collect(Collectors.toSet());
        Set<String> methods = orderPrices.stream().map(CalcOrderPriceResponse::getPayMethod).collect(Collectors.toSet());

        orderPrices.forEach(orderPrice -> {

            String payMethod = orderPrice.getPayMethod();
            BigDecimal waterPrice = orderPrice.getPrice();
            BigDecimal runPrice = orderPrice.getRunPrice();
            Integer buyNum = orderPrice.getBuyNum();
            Integer waterNum = orderPrice.getWaterNum();
            String deliveryType = orderPrice.getDeliveryType();

            // 三种支付方式
            switch (payMethod) {
                case "wechat":
                    price.set((int) (waterPrice.doubleValue() * 100) * buyNum);
                    break;
                case "coupon":
                    logger.info(orderId + " -> 执行优惠券支付, 仍需计算配送费");
                    break;
                case "mixed":
                    int newNum = buyNum - waterNum; // 优惠券抵扣后, 还需支付的水票数量
                    price.addAndGet((int) (waterPrice.doubleValue() * 100) * newNum);
                    break;
            }

            // 如果为上门配送, 则计算配送费
            if ("goDoor".equals(deliveryType)) {
                price.addAndGet((int) (runPrice.doubleValue() * 100 * buyNum));
            }
        });


        logger.info("订单价格 ->" + price);

        // 回写金额到数据库
        FtOrder ftOrder = orderMapper.selectByPrimaryKey(orderId);
        if (price.get() == 0) {
            ftOrder.setTotal(BigDecimal.ZERO);
        } else {
            ftOrder.setTotal(BigDecimal.valueOf(price.get() * 1.0 / 100));
        }
        orderMapper.updateByPrimaryKeySelective(ftOrder);

        if (price.get() == 0 && types.contains("selfGet")) {
            logger.info("订单金额为0, 直接返回成功");
            return new JSONObject();
        }

        // 构建微信支付请求体
        ObjectNode rootNode = objectMapper.createObjectNode();
        String wx_no = UUID.randomUUID().toString().replaceAll("-", "");
        rootNode.put("mchid", merchantId)
                .put("appid", wechatAppId)
                .put("description", "水票商品 - 智多星科技")
                .put("notify_url", "https://water.asugar.cn/notify")
                .put("out_trade_no", wx_no);
        rootNode.putObject("amount")
                .put("total", price.get());
        rootNode.putObject("payer")
                .put("openid", openId);
        objectMapper.writeValue(bos, rootNode);

        httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String res = EntityUtils.toString(response.getEntity());
        JSONObject jsonObject = JSON.parseObject(res);
        logger.info("微信支付回调信息 ->" + jsonObject);

        JSONObject resData = new JSONObject();
        resData.put("prepay_id", jsonObject.get("prepay_id"));
        // 支付方式
        resData.put("payMethod", StringUtils.join(methods, ","));
        resData.put("wx_no", wx_no);
        return resData;
    }

    @Override
    @SneakyThrows
    @Transactional
    public JSONObject refund(String wxNo) {

        logger.info("before refund ->" + wxNo);

        //解密
        wxNo = SecureUtil.aes("aEsva0zDHECg47P8SuPzmw==".getBytes()).decryptStr(wxNo);

        logger.info("after refund ->" + wxNo);

        Long userId = SecurityUtils.getUserId();

        // 获取订单金额
        OrderResponse orderResponse = orderMapper.selectOrderByWxNo(wxNo);
        double refundPrice = orderResponse.getTotal().doubleValue();

        // 自动获取微信证书, 第一次获取证书绕过鉴权
        CloseableHttpClient httpClient = buildHttpClient();

        // 构建订单数据
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        // 构建微信支付请求体
        ObjectNode rootNode = objectMapper.createObjectNode();
        String refundId = UUID.randomUUID().toString().replaceAll("-", "");
        rootNode.put("out_trade_no", wxNo);
        rootNode.put("out_refund_no", refundId);
        logger.info(wxNo + "发起退款，退款单号：" + refundId);
        rootNode.put("reason", "空桶退款");
        rootNode.putObject("amount")
                .put("refund", (int) (refundPrice * 100))
                .put("total", (int) (refundPrice * 100))
                .put("currency", "CNY");
        objectMapper.writeValue(bos, rootNode);
        httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));

        logger.info("退款请求体 ->" + bos.toString("UTF-8"));

        CloseableHttpResponse response = httpClient.execute(httpPost);
        String res = EntityUtils.toString(response.getEntity());
        logger.info("退款回调信息 ->" + res);
        JSONObject jsonObject = JSON.parseObject(res);
        Object status = jsonObject.get("status");
        if ("SUCCESS".equals(status) || "PROCESSING".equals(status)) {
            logger.info("退款成功, 将空桶数量-1");
            SysUser sysUser = userService.selectUserById(userId);
            sysUser.setBarrelNumber(sysUser.getBarrelNumber() - 1);
            //3已退款
            orderMapper.updateStatusByWxNo(wxNo, 3);
            int i = userService.updateUser(sysUser);
            if (i == 0) {
                logger.error("空桶数量-1失败，管理员请查看");
            }
        }
        return jsonObject;
    }

    @Override
    @SneakyThrows

    public Map<String, String> wakePay(String prepayId) {
        long time = DateUtil.currentSeconds();
        String nonstr = RandomUtil.randomString(20);
        String pack = "prepay_id=" + prepayId;
        String t = "wx0c0ff097756fc774\n";
        t += +time + "\n";
        t += nonstr + "\n";
        t += pack + "\n";

        // 签名
        PrivateKey privateKey = WxUtils.getPrivateKey();
        Signature rsa = Signature.getInstance("SHA256withRSA");
        rsa.initSign(privateKey);
        rsa.update(t.getBytes(StandardCharsets.UTF_8));
        byte[] sign = rsa.sign();

        String paySign = Base64.getEncoder().encodeToString(sign);

        HashMap<String, String> payMap = new HashMap<>();
        payMap.put("paySign", paySign);
        payMap.put("timeStamp", String.valueOf(time));
        payMap.put("nonceStr", nonstr);
        payMap.put("package", pack);
        payMap.put("signType", "RSA");
        return payMap;
    }

    @SneakyThrows
    private CloseableHttpClient buildHttpClient() {
        // 自动获取微信证书, 第一次获取证书绕过鉴权
        CertificatesManager instance = CertificatesManager.getInstance();
        String merchantId = configService.getCacheValue("merchantId");
        String serialNumber = configService.getCacheValue("serialNumber");
        String apiV3Key = configService.getCacheValue("apiV3Key");

        instance.putMerchant(merchantId, new WechatPay2Credentials(merchantId,
                        new PrivateKeySigner(serialNumber, WxUtils.getPrivateKey())),
                apiV3Key.getBytes(StandardCharsets.UTF_8));
        Verifier verifier = instance.getVerifier(merchantId);
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(merchantId, serialNumber, WxUtils.getPrivateKey())
                .withValidator(new WechatPay2Validator(verifier));
        return builder.build();
    }
}
