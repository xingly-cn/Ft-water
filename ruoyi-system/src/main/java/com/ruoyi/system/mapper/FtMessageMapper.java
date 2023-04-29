package com.ruoyi.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.ruoyi.system.entity.FtMessage;
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

    void confirms(@Param("homeId") Long homeId, @Param("userId") Long userId);

    List<MessageResponse> getMessageList(@Param("message") MessageRequest request);
}