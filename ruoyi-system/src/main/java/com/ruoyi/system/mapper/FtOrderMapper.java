package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.FtOrder;
import com.ruoyi.system.request.OrderRequest;
import com.ruoyi.system.response.CalcOrderPriceResponse;
import com.ruoyi.system.response.CountResponse;
import com.ruoyi.system.response.OrderResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/30 15:59
 */
@Repository
public interface FtOrderMapper {

    int deleteByPrimaryKey(Long id);

    List<CountResponse> getPriceByHomeIds(@Param("homeIds") Set<Long> homeIds,
                                          @Param("startTime")String startTime,
                                          @Param("endTime")String endTime);

    int insert(FtOrder order);

    int insertSelective(FtOrder order);

    FtOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FtOrder order);

    int updateByPrimaryKey(FtOrder order);

    List<OrderResponse> selectList(@Param("order") OrderRequest order);

    OrderResponse createOrderCQ(String orderId);

    List<CalcOrderPriceResponse> getOrderPrice(Long orderId);

    List<FtOrder> homeCount(@Param("userId") Long userId,
                            @Param("startTime") String startTime,
                            @Param("endTime") String endTime);

    String getGoodId(String orderId);

    String getUseNum(String orderId);

    OrderResponse selectOrderByWxNo(String wxNo);

    void updateStatusByWxNo(@Param("wxNo") String wxNo,
                            @Param("status") int status);

    void updateStatusByOrderId(@Param("orderId") Long orderId,
                               @Param("status") int status);
}