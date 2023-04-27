package com.ruoyi.system.service;


import com.ruoyi.system.entity.FtNotices;
import com.ruoyi.system.request.NoticesRequest;
import com.ruoyi.system.response.NoticesResponse;

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

    NoticesResponse selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FtNotices record);

    int updateByPrimaryKey(FtNotices record);

    Map<String, Object> getNoticesPage(NoticesRequest request);


    Map<String, Object> getNoticesByInput(long cur, long size);
}
