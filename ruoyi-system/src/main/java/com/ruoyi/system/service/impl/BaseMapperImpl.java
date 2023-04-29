package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/23 00:06
 */
public abstract class BaseMapperImpl<E, T, S, M extends BaseMapper<E>> extends ServiceImpl<M, E> {

    public Map<String, String> getCurrentUser() {
        //拦截器已经校验 这里不进行校验
        String token = getCurrentToken();
        return JwtUtils.getToken(token);
    }

    private HttpServletRequest request() {
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    }

    private String getCurrentToken() {
        String authorization = request().getHeader("Ft-Water");
        if (StringUtils.isEmpty(authorization)) {
            throw new ServiceException("登陆过期，请重新登陆");
        }
//        return JWTUtil.parseToken(authorization);
        return authorization;
    }

    public Map<String, Object> selectPage(IPage<T> page, S request) {
        this.customSelectPage(page, request);
        Map<String, Object> result = new ConcurrentHashMap<>();
        result.put("total", page.getTotal());
        result.put("size", page.getSize());
        result.put("current", page.getCurrent());
        result.put("list", page.getRecords());
        return result;
    }

    protected abstract void customSelectPage(IPage<T> page, S request);
}

