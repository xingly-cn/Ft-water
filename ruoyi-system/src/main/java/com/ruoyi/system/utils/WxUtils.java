package com.ruoyi.system.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;


/**
 * @Desc
 * @Author 方糖
 * @Date 2023-06-11 21:49
 **/
@Slf4j
public class WxUtils {
    public static final String file = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC+OOKpZYIEZswu\n" +
            "xlu0/iA9XKQV0DDFe9TjInXLEQtrj7sB5Kzfl6/2yF80xOUfQsOv1nnV6niokTzl\n" +
            "L9mXT0ImX5RO6z8ZFO6cvoyR8cw3RmG1Y4290HymP3j6rkz4kNfrR7LLdvvTuBrP\n" +
            "bxdq1nrOFFjZBuqNofWDLXoNhcJLDSE/A/65iPTZB6Mx+245d4SFUjm22uCG8OIX\n" +
            "5W6xjikiCJNCMVsoa48mcskwYp9MxJt4m0jDMbZu7xn8laZoOYsAZIXcmxq5Ee1b\n" +
            "2Z5y870SyHpiTbayf8J+uvpczyNSfc6fJVsL6UOVBbIu5fRTKZUxTCTpxPkcJlo6\n" +
            "x00SsxDpAgMBAAECggEBAJPjlhUtHhBd+5DCFrp4eIg8ZjWvTru6sqCEQiBGeCJ/\n" +
            "9FxcdSrAu8WZsqtMCPZs6cbjcIWnbXdgs39b3EePD1bWKG/S5ZOSepDSdGdjHeo7\n" +
            "heuIIsLYPJDj+CKtMJpvwe7iJ1eYgeDw2ac8L3CYn5FosErK3cCPklkSGjo5K/08\n" +
            "ONjd0pzWg6ZDy/06LdTbd0q0kQD3ydswriXPKJf85xvDuXoh7B8+5FR+TnHDa4z+\n" +
            "ihi3759jayPneYQsQdtZ1W8AZKw6T10Pa4YN27Qq8OdxhcJYwe3HSt3OKZtOloXJ\n" +
            "K7eoOZGAaErt8Jk0p+eiV5l4gzfp/SKb1tzkk5IwDIECgYEA4AepdUng2H8AV9mW\n" +
            "+Arj5eBK4uGJLPaWAlqyibYWUKs1Kp7MSxxd939ypvQUMqEMQhFuHXemChfpzrww\n" +
            "c6glkBc4M9xPtUnmBRelvifwmZcjHy3fo1HVaKLJ1js3vrDAkLi4cWop88l6JK8c\n" +
            "cqdyxRTI6zPqhplcUnGPfw7OTa0CgYEA2V4l8dk+Ex+B/1T8LmYozTMEYudxZp3E\n" +
            "ayRPzW64CoDR73ZHfPgFX+ztERJ0TSfzj4JB6GoeVLgJm5UYwgGTrLpbc0JW6f/g\n" +
            "5qvbmYpJF5LJLw01GSxJHec7Q1rodON/0RbChWrzt3DBEwe9SCFu3kfsHCpFa5MF\n" +
            "33eQtow3P60CgYAUJBw0E0RHsoNRYpqUCkaa8CNLaoNuq2ypIYZToy82aW4KOuSb\n" +
            "DB3Wlnq0T9VRB/GWLwL7WzpuReAbWgfxHfjij3PaFcCEx/QKGSopgmW7KT9bGkC0\n" +
            "6jKJibkjrYdBeZqWaEUMzUKgTEjS63tfQ3aqCUhW/P/kwfzu3I7OGYKqsQKBgQDD\n" +
            "pYYpxQumUrnDhE7nxHP9HmpYS1E6t85PUYOyuHFWotRS4Pc/eE3+JVbMAvxDZi5H\n" +
            "gUqpUrqyLmQXcX+zP5AX7DTb7V4liHZ5qT4jWOCpIymJ/C1K1x2ImNQyr+SDMTGe\n" +
            "lQnI5JcxCX//DPxFrDIKSFowj6ukQ9UJaqkKZfJkkQKBgAtN5VClMq5/RMDn3LKV\n" +
            "lpYLzsRvUq55VBRHJKQT7YyDiaLXLHDHIIJlyW+9vq3v8wW8LmFnCm9Vcbg7YOCC\n" +
            "t5r4lhMaFZblf8I4ONFzMf8nOa9I5sD4albdZiIprITBSe1jTkuKb3v1oItCmN7E\n" +
            "aEAq33RLvNVt89rQ12U1BCIF\n" +
            "-----END PRIVATE KEY-----\n";

    public static PrivateKey getPrivateKey() throws IOException {
        try {
            String privateKey = file.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }

    public static void main(String[] args) throws IOException {
        log.info(getPrivateKey().toString());
    }
}
