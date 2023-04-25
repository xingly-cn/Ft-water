package com.ruoyi.web.controller.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2023/4/22 23:20
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class NoticesRequest extends PageRequest{

    private Long homeId;

    private Long schoolId;

    private Long userId;
}
