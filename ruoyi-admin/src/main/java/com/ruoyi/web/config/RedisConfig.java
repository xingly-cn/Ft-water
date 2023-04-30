//package com.ruoyi.web.config;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CachingConfigurationSelector;
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.time.Duration;
//
///**
// * Created by IntelliJ IDEA.
// * @Author : 镜像
// * @create 2023/3/2 14:22
// */
//
//
//public class RedisConfig extends CachingConfigurationSelector {
//
//    @Bean
//    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){
//
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        template.setConnectionFactory(factory);
//
//        //设置“key"的序列化方式
//        template.setKeySerializer(new StringRedisSerializer());
//
//        //设置“值”的序列化方式
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//
//        //设置“hash”类型数据的序列化方式
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//
//        return template;
//    }
//}
