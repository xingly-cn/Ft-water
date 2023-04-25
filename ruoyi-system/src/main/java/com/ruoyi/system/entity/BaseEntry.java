package com.ruoyi.system.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/30 22:58
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntry implements Serializable {

    private static final long serialVersionUID = -1884985824596457312L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "是否删除")
    private Boolean deleted;
}
