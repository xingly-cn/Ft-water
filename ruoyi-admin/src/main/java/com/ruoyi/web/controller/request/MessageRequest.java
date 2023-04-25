package com.ruoyi.web.controller.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Desc
 * @Author 方糖
 * @Date 2023-04-01 15:32
 **/
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class MessageRequest extends PageRequest {

    private Long homeId;

    private Long userId;

    private Boolean confirm;
}
