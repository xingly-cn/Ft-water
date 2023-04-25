package com.ruoyi.web.controller.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.web.entity.FtHome;
import com.ruoyi.web.entity.FtUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/18 21:44
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class HomeResponse extends FtHome {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<HomeResponse> children;

    private String schoolName;

    private List<FtUser> users;
}
