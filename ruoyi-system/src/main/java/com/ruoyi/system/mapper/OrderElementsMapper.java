package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.OrderElements;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/5/2 15:27
 */
public interface OrderElementsMapper {

    int insert(OrderElements orderElements);

    int insertSelective(OrderElements orderElements);

}
