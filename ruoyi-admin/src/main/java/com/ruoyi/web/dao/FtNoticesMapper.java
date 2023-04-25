package com.ruoyi.web.dao;

import com.asugar.ftwaterdelivery.controller.request.NoticesRequest;
import com.asugar.ftwaterdelivery.controller.response.NoticesResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.web.entity.FtNotices;
import org.apache.ibatis.annotations.Param;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/19 21:48
 */
public interface FtNoticesMapper extends BaseMapper<FtNotices> {

    int deleteByPrimaryKey(Long id);

    int insert(FtNotices record);

    int insertSelective(FtNotices record);

    NoticesResponse selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FtNotices record);

    int updateByPrimaryKey(FtNotices record);

    Page<NoticesResponse> selectPage(@Param("page") IPage<NoticesResponse> page,
                                     @Param("notices") NoticesRequest request);

}