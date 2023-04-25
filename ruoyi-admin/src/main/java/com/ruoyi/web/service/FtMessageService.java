package com.ruoyi.web.service;

import com.ruoyi.web.controller.request.MessageRequest;
import com.ruoyi.web.controller.response.MessageResponse;
import com.ruoyi.web.entity.FtMessage;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/19 21:48
 */
public interface FtMessageService {
    int deleteByPrimaryKey(Long id);
    int insert(FtMessage record);
    int insertSelective(FtMessage record);
    MessageResponse selectByPrimaryKey(Long id);
    int updateByPrimaryKeySelective(FtMessage record);
    int updateByPrimaryKey(FtMessage record);
    Boolean addMessage(Long id);

    Map<String, Object> getMessagePage(MessageRequest request);
}
