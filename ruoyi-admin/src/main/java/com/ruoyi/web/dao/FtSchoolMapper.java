package com.ruoyi.web.dao;

import com.asugar.ftwaterdelivery.controller.request.SchoolRequest;
import com.asugar.ftwaterdelivery.controller.response.IndexCountResponse;
import com.asugar.ftwaterdelivery.controller.response.SchoolResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.web.entity.FtSchool;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* Created by IntelliJ IDEA.
* @Author : 镜像
* @create 2023/3/30 15:59
*/
@Repository
public interface FtSchoolMapper extends BaseMapper<FtSchool> {

    int deleteByPrimaryKey(Long id);

    int insertSelective(FtSchool record);

    FtSchool selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FtSchool record);

    int updateByPrimaryKey(FtSchool record);

    List<IndexCountResponse> countIndex(@Param("time")String time,
                                        @Param("schoolIds")List<Long> schoolIds);

    Page<SchoolResponse> selectPage(@Param("page") IPage<SchoolResponse> page,
                                    @Param("school") SchoolRequest request);
}