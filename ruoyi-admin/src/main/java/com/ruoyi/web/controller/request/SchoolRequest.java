package com.ruoyi.web.controller.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Desc
 * @Author 方糖
 * @Date 2023-04-01 15:40
 **/
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class SchoolRequest extends PageRequest {

    private String schoolName;
}
