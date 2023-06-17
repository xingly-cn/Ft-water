package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TextMessage;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/6/17 18:14
 */
@Repository
public interface TextMessageMapper {

    void insertTextMessage(TextMessage textMessage);
}
