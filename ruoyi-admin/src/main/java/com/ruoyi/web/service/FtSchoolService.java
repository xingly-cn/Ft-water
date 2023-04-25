package com.ruoyi.web.service;

import com.ruoyi.web.controller.request.SchoolRequest;
import com.ruoyi.web.entity.FtSchool;

import java.util.List;
import java.util.Map;

/**
* Created by IntelliJ IDEA.
* @Author : 镜像
* @create 2023/3/30 15:59
*/
public interface FtSchoolService{

    Boolean deleteByPrimaryKey(Long id);

    Boolean addSchool(FtSchool record);

    FtSchool selectByPrimaryKey(Long id);

    Boolean updateSchool(FtSchool record);

    Map<String, Object> getSchoolPage(SchoolRequest request);

    List<FtSchool> selectSchoolList(FtSchool School);

}
