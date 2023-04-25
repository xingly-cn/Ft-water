package com.ruoyi.system.service;

import com.ruoyi.system.request.IndexRequest;
import com.ruoyi.system.response.IndexCountResponse;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/31 22:39
 */
public interface IndexService {

    List<IndexCountResponse> countEveryDay(IndexRequest request);
}
