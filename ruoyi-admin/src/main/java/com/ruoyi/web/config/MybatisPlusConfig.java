//package com.ruoyi.web.config;
//
//import com.baomidou.mybatisplus.annotation.DbType;
//import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import java.util.Collections;
//
//
//@EnableTransactionManagement
//@Configuration
//@MapperScan("com.ruoyi.system.mapper")
//public class MybatisPlusConfig {
//
//    @Bean
//    public PaginationInnerInterceptor paginationInterceptor() {
//        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
//        paginationInterceptor.setDbType(DbType.MYSQL);
//        paginationInterceptor.setOptimizeJoin(true);
//        return paginationInterceptor;
//    }
//
//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor(){
//        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
//        mybatisPlusInterceptor.setInterceptors(Collections.singletonList(paginationInterceptor()));
//        return mybatisPlusInterceptor;
//    }
//}