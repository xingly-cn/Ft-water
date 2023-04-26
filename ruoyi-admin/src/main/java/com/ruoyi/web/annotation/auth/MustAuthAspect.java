package com.ruoyi.web.annotation.auth;


import com.ruoyi.system.exception.ServiceException;
import com.ruoyi.system.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * @Desc 强制登录切面
 * @Author 方糖
 * @Date 2023-03-30 14:57
 **/
@Aspect
@Component
@Slf4j
public class MustAuthAspect {

    @Pointcut("@annotation(com.ruoyi.web.annotation.auth.MustAuth)")
    public void checkLogin() {
    }

    @Before("checkLogin()")
    public void doBefore() {
        log.info("[日志记录] 开始-------------------------");
        long left = System.currentTimeMillis();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        log.info(" 请求地址：{}", request.getRequestURI());
        log.info(" Token：{}", request.getHeader("Ft-Water"));

        String token = request.getHeader("Ft-Water");
        boolean verify = JwtUtils.verify(token);
        long right = System.currentTimeMillis();

        log.info(" 用户登录状态：{}", verify);
        log.info(" 请求耗时：{}ms", right - left);
        log.info("[日志记录] 结束-------------------------\n");

        if (!verify) {
            throw new ServiceException("用户未登录");
        }
    }

}
