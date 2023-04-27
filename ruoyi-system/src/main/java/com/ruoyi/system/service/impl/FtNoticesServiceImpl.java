package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.system.entity.FtNotices;
import com.ruoyi.system.mapper.FtNoticesMapper;
import com.ruoyi.system.request.NoticesRequest;
import com.ruoyi.system.response.NoticesResponse;
import com.ruoyi.system.service.FtNoticesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/19 21:48
 */
@Service
public class FtNoticesServiceImpl extends BaseMapperImpl<FtNotices, NoticesResponse, NoticesRequest, FtNoticesMapper> implements FtNoticesService {

    @Resource
    private FtNoticesMapper ftNoticesMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return ftNoticesMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(FtNotices record) {
        return ftNoticesMapper.insert(record);
    }

    @Override
    public int insertSelective(FtNotices record) {
        return ftNoticesMapper.insertSelective(record);
    }

    @Override
    public NoticesResponse selectByPrimaryKey(Long id) {
        return ftNoticesMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(FtNotices record) {
        return ftNoticesMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(FtNotices record) {
        return ftNoticesMapper.updateByPrimaryKey(record);
    }

    @Override
    public Map<String, Object> getNoticesPage(NoticesRequest request) {
        IPage<NoticesResponse> page = new Page<>(request.getPage(), request.getSize());
        return selectPage(page, request);
    }

    @Override
    public Map<String, Object> getNoticesByInput(long cur, long size) {
        IPage<FtNotices> page = new Page<>(cur, size);
        ftNoticesMapper.selectPage(page, new QueryWrapper<FtNotices>().eq("type", 0).orderByDesc("createTime"));
        Map<String, Object> result = new HashMap<>();
        result.put("total", page.getTotal());
        result.put("list", page.getRecords());
        result.put("size", page.getSize());
        result.put("cur", page.getCurrent());
        return result;
    }


    @Override
    protected void customSelectPage(IPage<NoticesResponse> page, NoticesRequest request) {
        ftNoticesMapper.selectPage(page, request);
    }
}
