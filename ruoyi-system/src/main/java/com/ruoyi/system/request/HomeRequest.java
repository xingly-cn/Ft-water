package com.ruoyi.system.request;

import com.ruoyi.system.entity.FtHome;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/18 23:18
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class HomeRequest extends FtHome {

    private Integer number;

    private Long userId;
}
