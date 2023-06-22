package com.ruoyi.web.controller.common;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Desc
 * @Author 方糖
 * @Date 2023-06-22 20:59
 **/
@Api(tags = "短信控制")
@RestController
@RequestMapping("v1/admin/sms")
public class SmsController {

    @Resource
    private RedisCache redisCache;


    @GetMapping("sendCode")
    @ApiOperation("发送验证码")
    public AjaxResult sendCode(String phone) {
        String code = RandomUtil.randomNumbers(6);
        redisCache.setCacheObject("sms:"+phone, code, 5, TimeUnit.MINUTES);
        HttpUtil.post("http://open2.ucpaas.com/sms-server/variablesms", "{\n" +
                "    \"clientid\": \"b1acl7\",\n" +
                "    \"password\": \"6b5b54693bba1df879f23505d1d14dec\",\n" +
                "    \"mobile\": \""+phone+"\",\n" +
                "    \"templateid\": \"963\",\n" +
                "    \"param\": \""+code+"\"\n" +
                "}");
        return AjaxResult.success("发送成功");
    }

}
