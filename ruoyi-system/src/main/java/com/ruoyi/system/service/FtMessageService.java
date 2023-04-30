package com.ruoyi.system.service;


import com.ruoyi.system.domain.FtMessage;
import com.ruoyi.system.request.MessageRequest;
import com.ruoyi.system.response.MessageResponse;

import java.util.List;

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

    List<MessageResponse> getMessageList(MessageRequest request);
}
