package com.ruoyi.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/22 23:44
 */
public interface MyBaseService<T> extends IService<T> {
    Map<String, Object> selectPage(IPage<T> page, Object request);
}
