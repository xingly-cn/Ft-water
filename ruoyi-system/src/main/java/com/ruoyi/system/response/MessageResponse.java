package com.ruoyi.system.response;


import com.ruoyi.system.entity.FtMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2023/4/20 21:10
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class MessageResponse extends FtMessage {

    private String homeName;

    private String userName;
}
