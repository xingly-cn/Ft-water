package com.ruoyi.system.service;


import com.ruoyi.system.entity.FtMessage;
import com.ruoyi.system.request.MessageRequest;
import com.ruoyi.system.response.MessageResponse;

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
