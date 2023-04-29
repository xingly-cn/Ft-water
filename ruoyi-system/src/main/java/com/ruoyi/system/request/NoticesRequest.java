package com.ruoyi.system.request;

import com.ruoyi.system.entity.FtNotices;
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
public class NoticesRequest extends FtNotices {

    private Long homeId;

    private Long schoolId;

    private Long userId;
}
