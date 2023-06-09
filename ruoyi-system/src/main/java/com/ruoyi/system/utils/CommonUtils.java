package com.ruoyi.system.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.config.ApplicationContextProvider;
import com.ruoyi.system.domain.TextMessage;
import com.ruoyi.system.mapper.TextMessageMapper;
import com.ruoyi.system.service.impl.SysConfigServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;


/**
 * @Author 方糖
 * @Description 通用工具类
 **/
@Component
public class CommonUtils {

    private static final String ENDPOINT;

    private static final String RESULT_URL;

    private static final String ACCESS_KEY_ID;

    private static final String ACCESS_KEY_SECRET;

    private static final String MSG_APPID;
    private static final String MSG_APPSID;
    private static final String MSG_TOKEN;

    private static final TextMessageMapper messageMapper;

    static {
        SysConfigServiceImpl configService = ApplicationContextProvider.getBean(SysConfigServiceImpl.class);
        ENDPOINT = configService.getCacheValue("ENDPOINT");
        RESULT_URL = configService.getCacheValue("RESULT_URL");
        ACCESS_KEY_ID = configService.getCacheValue("ACCESS_KEY_ID");
        ACCESS_KEY_SECRET = configService.getCacheValue("ACCESS_KEY_SECRET");
        MSG_APPID = configService.getCacheValue("MSG_APPID");
        MSG_APPSID = configService.getCacheValue("MSG_APPSID");
        MSG_TOKEN = configService.getCacheValue("MSG_TOKEN");
        messageMapper = ApplicationContextProvider.getBean(TextMessageMapper.class);
    }

    /**
     * @param file 文件上传
     * @param type 类型：图片, 文件
     */
    public static String uploadFile(MultipartFile file, String type) {
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        String path = type + "/" + UUID.randomUUID() + ".jpg";
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest("ft-water", path, file.getInputStream());
            ossClient.putObject(putObjectRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return RESULT_URL + path;
    }

    /**
     * 短信发送
     *
     * @param phone
     * @param message
     * @return
     */
    public static String buildMessage(String phone, String message) {
        TextMessage textMessage = new TextMessage();
        textMessage.setPhone(phone);
        textMessage.setUserId(SecurityUtils.getUserId());
        textMessage.setCode("验证码");
        messageMapper.insertTextMessage(textMessage);
        //todo
        return "";
    }

    public static String stringToHex(String input) {
        char[] chars = input.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(Integer.toHexString(ch));
        }
        return hex.toString();
    }

    public static String hexToString(String input) {
        int len = input.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(input.charAt(i), 16) << 4)
                    + Character.digit(input.charAt(i + 1), 16));
        }
        return new String(data, StandardCharsets.UTF_8);
    }

    /**
     * 获取手机号后6位
     * @param phone 手机号
     * @return 后6位
     */
    public static String getPhoneLast6(String phone) {
        if (StringUtils.isEmpty(phone))
            return phone;
        if (phone.length() < 6)
            return phone;
        return phone.substring(phone.length() - 6);
    }

}
