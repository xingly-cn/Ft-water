package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.OrderElements;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/5/2 15:27
 */
public interface OrderElementsMapper {

    int insert(OrderElements orderElements);

    int insertSelective(OrderElements orderElements);

    Boolean insertBatch(List<OrderElements> orderElements);

    List<OrderElements> selectElementsByOrderId(Long orderId);


}
