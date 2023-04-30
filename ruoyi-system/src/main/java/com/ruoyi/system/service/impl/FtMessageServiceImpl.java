package com.ruoyi.system.service.impl;


import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.FtMessage;
import com.ruoyi.system.mapper.FtMessageMapper;
import com.ruoyi.system.request.MessageRequest;
import com.ruoyi.system.response.MessageResponse;
import com.ruoyi.system.service.FtHomeService;
import com.ruoyi.system.service.FtMessageService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FtMessageServiceImpl implements FtMessageService {

    @Resource
    private FtMessageMapper ftMessageMapper;

    @Autowired
    private FtHomeService homeService;

    @Autowired
    private SysUserServiceImpl userService;

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
        Long userId = SecurityUtils.getUserId();
        SysUser user = userService.selectUserById(userId);
        Long homeId = user.getHomeId();
        //驳回 确认
        confirms(homeId, userId);
        return homeService.addNumberByHomeId(homeId, message.getNumber());
    }

    @Override
    public List<MessageResponse> getMessageList(MessageRequest request) {
        return ftMessageMapper.getMessageList(request);
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
}
