package com.ruoyi.web.service;

import com.ruoyi.web.controller.request.NoticesRequest;
import com.ruoyi.web.entity.FtNotices;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/19 21:48
 */
public interface FtNoticesService {

    int deleteByPrimaryKey(Long id);

    int insert(FtNotices record);

    int insertSelective(FtNotices record);

    com.asugar.ftwaterdelivery.controller.response.NoticesResponse selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FtNotices record);

    int updateByPrimaryKey(FtNotices record);

    Map<String, Object> getNoticesPage(NoticesRequest request);
}
