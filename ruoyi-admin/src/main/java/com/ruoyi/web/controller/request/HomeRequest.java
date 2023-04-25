package com.ruoyi.web.controller.request;

import lombok.Data;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/18 23:18
 */
@Data
@ToString
public class HomeRequest {

    private Long id;

    private Integer number;

    private Long userId;
}
