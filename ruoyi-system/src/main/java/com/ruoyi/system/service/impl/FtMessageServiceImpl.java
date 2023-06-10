package com.ruoyi.system.service.impl;


import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.FtHome;
import com.ruoyi.system.domain.FtMessage;
import com.ruoyi.system.mapper.FtMessageMapper;
import com.ruoyi.system.request.MessageRequest;
import com.ruoyi.system.response.MessageResponse;
import com.ruoyi.system.service.FtHomeService;
import com.ruoyi.system.service.FtMessageService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private FtHomeServiceImpl homeServiceImpl;

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
        MessageResponse response = ftMessageMapper.selectByPrimaryKey(id);
        if (response != null) {
            List<FtHome> homes = homeServiceImpl.getHomes();
            getMessageHomeName(homes, response);
        }
        return response;
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
    @CacheEvict(value = "home", allEntries = true)
    public Boolean confirmMessage(Long id) {
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
        confirms(id, userId);
        return homeService.addNumberByHomeId(homeId, message.getNumber());
    }

    @Override
    public List<MessageResponse> getMessageList(MessageRequest request) {
        List<MessageResponse> messageList = ftMessageMapper.getMessageList(request);
        if (CollectionUtils.isNotEmpty(messageList)) {
            List<FtHome> homes = homeServiceImpl.getHomes();
            for (MessageResponse message : messageList) {
                getMessageHomeName(homes, message);
            }
        }
        return messageList;
    }

    @Override
    @Transactional
    public Boolean refuseMessage(Long id) {
        return ftMessageMapper.refuseMessage(id)>0;
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

    private void confirms(Long id, Long userId) {
        //确认收获 一楼确认收获 无收获的
        ftMessageMapper.confirms(id, userId);
    }

    private void getMessageHomeName(List<FtHome> homes, MessageResponse message) {
        FtHome home = homeServiceImpl.getTopHome(homes, message.getHomeId());
        if (home != null) {
            String homeName = homes.stream().filter(h -> h.getId().equals(message.getHomeId())).findFirst().orElse(new FtHome()).getName();
            message.setHomeName(home.getName()+"/"+homeName);
        }
    }
}
