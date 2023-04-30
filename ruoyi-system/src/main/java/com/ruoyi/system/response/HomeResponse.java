package com.ruoyi.system.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.FtHome;
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

    private List<SysUser> users;
}
