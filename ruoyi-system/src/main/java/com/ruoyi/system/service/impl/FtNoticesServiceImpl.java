package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.FtNotices;
import com.ruoyi.system.mapper.FtNoticesMapper;
import com.ruoyi.system.request.NoticesRequest;
import com.ruoyi.system.response.NoticesResponse;
import com.ruoyi.system.service.FtNoticesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/19 21:48
 */
@Service
public class FtNoticesServiceImpl implements FtNoticesService {

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
    public List<NoticesResponse> getNoticesList(NoticesRequest request) {
        return ftNoticesMapper.selectList(request);
    }

    @Override
    public List<NoticesResponse> getNoticesByInput(NoticesRequest request) {
        request.setType(0);
        return ftNoticesMapper.selectList(request);
    }

    public FtNotices selectLastByHomeIdAndOrderType(Long homeId, Integer orderType) {
        return ftNoticesMapper.selectLastByHomeIdAndOrderType(homeId, orderType);
    }
}
