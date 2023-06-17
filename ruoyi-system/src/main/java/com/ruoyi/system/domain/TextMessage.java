package com.ruoyi.system.domain;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/6/17 18:08
 */
@Data
@ToString
public class TextMessage {

    private Long id;

    private String code;

    private Long userId;

    private String phone;

    private Date createTime;
}
