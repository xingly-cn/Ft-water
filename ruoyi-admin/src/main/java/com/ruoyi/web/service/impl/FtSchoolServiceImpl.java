package com.ruoyi.web.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.web.controller.request.SchoolRequest;
import com.ruoyi.web.controller.response.SchoolResponse;
import com.ruoyi.web.dao.FtSchoolMapper;
import com.ruoyi.web.entity.FtSchool;
import com.ruoyi.web.service.FtSchoolService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/30 15:59
 */
@Service
public class FtSchoolServiceImpl extends BaseMapperImpl<FtSchool, SchoolResponse, SchoolRequest, FtSchoolMapper> implements FtSchoolService {

    @Resource
    private FtSchoolMapper ftSchoolMapper;

    @Override
    @CacheEvict(value = "school", key = "#id")
    public Boolean deleteByPrimaryKey(Long id) {
        return ftSchoolMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    @CachePut(value = "school", key = "#record.id")
    public Boolean addSchool(FtSchool record) {
        return ftSchoolMapper.insertSelective(record) > 0;
    }

    @Override
    @Cacheable(value = "school", key = "#id", unless = "#result == null")
    public FtSchool selectByPrimaryKey(Long id) {
        return ftSchoolMapper.selectByPrimaryKey(id);
    }

    @Override
    @CachePut(value = "school", key = "#record.id")
    public Boolean updateSchool(FtSchool record) {
        return ftSchoolMapper.updateByPrimaryKeySelective(record) > 0;
    }

    @Override
    public Map<String, Object> getSchoolPage(SchoolRequest request) {
        IPage<SchoolResponse> page = new Page<>(request.getPage(), request.getSize());
        return selectPage(page, request);
    }

    @Override
    public List<FtSchool> selectSchoolList(FtSchool School) {
        return ftSchoolMapper.selectList(new QueryWrapper<>(School));
    }

    @Cacheable(value = "school", key = "#ids", unless = "#result == null")
    public List<FtSchool> selectSchoolListByIds(List<Long> ids) {
        return ftSchoolMapper.selectBatchIds(ids);
    }

    @Override
    protected void customSelectPage(IPage<SchoolResponse> page, SchoolRequest request) {
        ftSchoolMapper.selectPage(page, request);
    }
}
