package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.Api;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2023/5/1 15:17
 */
@Data
@ToString
@Api("用户楼栋管理")
@Builder
public class UserHome implements Serializable {

    private static final long serialVersionUID = 7802128654790973740L;

    private Long userId;

    private Long homeId;

    @JsonInclude
    private String userName;

    @Tolerate
    public UserHome() {}
}
