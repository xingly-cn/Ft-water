package com.ruoyi.system.service.impl;


import com.ruoyi.system.entity.FtSchool;
import com.ruoyi.system.mapper.FtSchoolMapper;
import com.ruoyi.system.request.SchoolRequest;
import com.ruoyi.system.response.SchoolResponse;
import com.ruoyi.system.service.FtSchoolService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/30 15:59
 */
@Service
public class FtSchoolServiceImpl implements FtSchoolService {

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
    public List<SchoolResponse> selectSchoolList(SchoolRequest request) {
        return ftSchoolMapper.selectList(request);
    }

    //以每一个返回的对象的id作为key，返回的对象作为value
    @Cacheable(value = "school", key = "#result[ids]", unless = "#result == null")
    public List<FtSchool> selectSchoolListByIds(List<Long> ids) {
        return ftSchoolMapper.selectBatchIds(ids);
    }

}
