package com.ruoyi.system.user;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.utils.WxUtils;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

/**
 * @Desc
 * @Author 方糖
 * @Date 2023-06-11 21:43
 **/
@RestController
@RequestMapping("v1/wechat/pay")
@Api(tags = "支付模块")
@Slf4j
public class JsApiPayController {
    @GetMapping("/createNo")
    @ApiOperation("JSAPI下单")
    public AjaxResult createPay() throws NotFoundException, IOException, GeneralSecurityException, HttpCodeException {

        String openId = SecurityUtils.getOpenId();

        // 自动获取微信证书, 第一次获取证书绕过鉴权
        CertificatesManager instance = CertificatesManager.getInstance();
        String merchantId = "1644099333";
        String serialNumber = "43D14D8650152DE650A0F83F87017298C3D1372B";
        String apiV3Key = "NorthWeixinPayZhiDuoXing12345678";
        String wechatAppId = "wx0c0ff097756fc774";
        instance.putMerchant(merchantId, new WechatPay2Credentials(merchantId, new PrivateKeySigner(serialNumber, WxUtils.getPrivateKey())), apiV3Key.getBytes(StandardCharsets.UTF_8));
        Verifier verifier = instance.getVerifier(merchantId);
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create().withMerchant(merchantId, serialNumber, WxUtils.getPrivateKey()).withValidator(new WechatPay2Validator(verifier));
        CloseableHttpClient httpClient = builder.build();

        // 构建订单数据
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        int price = 1;      // 商品价格

        ObjectNode rootNode = objectMapper.createObjectNode();
        String wx_no = UUID.randomUUID().toString().replaceAll("-", "");
        rootNode.put("mchid", merchantId)
                .put("appid", wechatAppId)
                .put("description", "水票商品 - 智多星科技")
                .put("notify_url", "https://water.asugar.cn/notify")
                .put("out_trade_no", wx_no);
        rootNode.putObject("amount")
                .put("total", price);
        rootNode.putObject("payer")
                .put("openid", openId);
        objectMapper.writeValue(bos, rootNode);

        httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String res = EntityUtils.toString(response.getEntity());
        JSONObject jsonObject = new JSONObject(res);
        log.info(jsonObject.toString());
        return AjaxResult.success(jsonObject.get("prepay_id"));
    }


    @ApiOperation("paySign")
    @GetMapping("wakePay")
    public AjaxResult wakePay(String prepay_id) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        long time = DateUtil.currentSeconds();
        String nonstr = RandomUtil.randomString(20);
        String pack = "prepay_id=" + prepay_id;
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

        HashMap<Object, Object> payMap = new HashMap<>();
        payMap.put("paySign", paySign);
        payMap.put("timeStamp", "" + time);
        payMap.put("nonceStr", nonstr);
        payMap.put("package", pack);
        payMap.put("signType", "RSA");

        return AjaxResult.success(payMap);
    }
}
