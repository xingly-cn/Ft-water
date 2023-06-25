package com.ruoyi.system.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/6/12 23:44
 */
@Data
@ToString
@Builder
public class OrderHomeCountResponse {

    private Long homeId;

    private String homeName;

    private Long userId;

    private Integer count;

    private Integer waitCount;

    private Integer waterCount;

    private Integer waterWaiteCount;

    @Tolerate
    public OrderHomeCountResponse() {}
}
