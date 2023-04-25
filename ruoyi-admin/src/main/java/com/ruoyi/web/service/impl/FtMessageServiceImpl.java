package com.ruoyi.web.service.impl;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.web.controller.request.MessageRequest;
import com.ruoyi.web.controller.response.MessageResponse;
import com.ruoyi.web.dao.FtMessageMapper;
import com.ruoyi.web.entity.FtMessage;
import com.ruoyi.web.entity.FtUser;
import com.ruoyi.web.service.FtHomeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/19 21:48
 */
@Service
public class FtMessageServiceImpl extends BaseMapperImpl<FtMessage, MessageResponse, MessageRequest, FtMessageMapper> implements FtMessageService {

    @Resource
    private FtMessageMapper ftMessageMapper;

    @Autowired
    private FtHomeService homeService;

    @Autowired
    private FtUserServiceImpl userService;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return ftMessageMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(FtMessage record) {
        return ftMessageMapper.insert(record);
    }

    @Override
    public int insertSelective(FtMessage record) {
        return ftMessageMapper.insertSelective(record);
    }

    @Override
    @Cacheable(value = "message", key = "#id", unless = "#result==null")
    public MessageResponse selectByPrimaryKey(Long id) {
        return ftMessageMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(FtMessage record) {
        return ftMessageMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(FtMessage record) {
        return ftMessageMapper.updateByPrimaryKey(record);
    }

    @Override
    public Boolean addMessage(Long id) {
        //确认收获
        FtMessage message = selectByPrimaryKey(id);
        if (message == null) {
            throw new SecurityException("该消息不存在");
        }
        //找到对应的楼 一人确认收获 一楼确认收获
        Long userId = Long.parseLong(getCurrentUser().get("userId"));
        FtUser user = userService.selectByPrimaryKey(userId);
        Long homeId = Long.parseLong(user.getHomeId());
        confirms(homeId, userId);
        return homeService.addNumberByHomeId(homeId, message.getNumber());
    }

    @Override
    public Map<String, Object> getMessagePage(MessageRequest request) {
        IPage<MessageResponse> page = new Page<>(request.getPage(), request.getSize());
        return selectPage(page, request);
    }

    public void addMessages(List<FtMessage> messages) {
        if (CollectionUtils.isEmpty(messages)) {
            return;
        }
        ftMessageMapper.insertBatch(messages);
    }

    public void updateMessages(List<FtMessage> messages) {
        if (CollectionUtils.isEmpty(messages)) {
            return;
        }
        ftMessageMapper.updateBatch(messages);
    }

    private void confirms(Long homeId, Long userId) {
        //确认收获 一楼确认收获 无收获的
        ftMessageMapper.confirms(homeId, userId);
    }

    @Override
    protected void customSelectPage(IPage<MessageResponse> page, MessageRequest request) {
        ftMessageMapper.selectPage(page, request);
    }
}
