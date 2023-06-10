package com.ruoyi.system.mapper;


import com.ruoyi.system.domain.FtMessage;
import com.ruoyi.system.request.MessageRequest;
import com.ruoyi.system.response.MessageResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/19 21:48
 */
public interface FtMessageMapper {

    int deleteByPrimaryKey(Long id);

    int insert(FtMessage record);

    int insertSelective(FtMessage record);

    MessageResponse selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FtMessage record);

    int updateByPrimaryKey(FtMessage record);

    void insertBatch(@Param("messages") List<FtMessage> messages);

    void updateBatch(@Param("messages") List<FtMessage> messages);

    void confirms(@Param("id") Long id,
                  @Param("userId") Long userId);

    List<MessageResponse> getMessageList(@Param("message") MessageRequest request);

    int refuseMessage(Long id);
}