package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.web.controller.request.NoticesRequest;
import com.ruoyi.web.dao.FtNoticesMapper;
import com.ruoyi.web.entity.FtNotices;
import com.ruoyi.web.service.FtNoticesService;
import com.ruoyi.web.service.impl.BaseMapperImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/19 21:48
 */
@Service
public class FtNoticesServiceImpl extends BaseMapperImpl<FtNotices, com.asugar.ftwaterdelivery.controller.response.NoticesResponse, NoticesRequest, FtNoticesMapper> implements FtNoticesService {

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
    public com.asugar.ftwaterdelivery.controller.response.NoticesResponse selectByPrimaryKey(Long id) {
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
        IPage<com.asugar.ftwaterdelivery.controller.response.NoticesResponse> page = new Page<>(request.getPage(), request.getSize());
        return selectPage(page, request);
    }

    @Override
    protected void customSelectPage(IPage<com.asugar.ftwaterdelivery.controller.response.NoticesResponse> page, NoticesRequest request) {
        ftNoticesMapper.selectPage(page, request);
    }
}
