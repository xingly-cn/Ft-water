package com.ruoyi.web.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.ruoyi.web.config.ApplicationContextProvider;
import com.ruoyi.web.service.impl.ConfigServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    static {
        ConfigServiceImpl configService = ApplicationContextProvider.getBean(ConfigServiceImpl.class);
        ENDPOINT = configService.getConfig("ENDPOINT");
        RESULT_URL = configService.getConfig("RESULT_URL");
        ACCESS_KEY_ID = configService.getConfig("ACCESS_KEY_ID");
        ACCESS_KEY_SECRET = configService.getConfig("ACCESS_KEY_SECRET");
        MSG_APPID = configService.getConfig("MSG_APPID");
        MSG_APPSID = configService.getConfig("MSG_APPSID");
        MSG_TOKEN = configService.getConfig("MSG_TOKEN");
    }

    /**
     * @param file   文件上传
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
     * @param phone
     * @param message
     * @return
     */
    public static String buildMessage(String phone, String message) {
        //todo
        return "";
    }

}
