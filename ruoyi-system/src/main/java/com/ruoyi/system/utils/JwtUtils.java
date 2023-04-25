package com.ruoyi.system.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author 方糖
 * @Description JWT工具类
 **/
@Component
@Slf4j
public class JwtUtils {
    private static String secret;
    private static long expire;

    @Value("${config.jwt.secret}")
    public void setSecret(String secret) {
        JwtUtils.secret = secret;
    }
    @Value("${config.jwt.expire}")
    public void setExpire(long expire) {
        JwtUtils.expire = expire;
    }

    public static String createToken (String userId, String schoolId, String userType) {
        Date date = new Date(System.currentTimeMillis() + expire * 1000);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withClaim("userId", userId)
                .withClaim("schoolId", schoolId)
                .withClaim("userType", userType)
                .withIssuedAt(new Date())
                .withExpiresAt(date)
                .sign(algorithm);
    }

    public static boolean verify (String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            verifier.verify(token);
        }catch (JWTDecodeException e) {
            return false;
        }
        return true;
    }

    public static Map<String,String> getToken (String token) {
        Map<String,String> result = new ConcurrentHashMap<>(4);
        DecodedJWT decode = JWT.decode(token);
        String userId = decode.getClaim("userId").asString();
        String schoolId = decode.getClaim("schoolId").asString();
        String userType = decode.getClaim("userType").asString();
        result.put("userId", userId);
        result.put("schoolId", schoolId);
        result.put("userType", userType);
        return result;
    }

}
