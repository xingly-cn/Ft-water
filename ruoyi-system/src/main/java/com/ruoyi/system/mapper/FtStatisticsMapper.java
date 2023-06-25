package com.ruoyi.system.mapper;


import com.ruoyi.system.domain.FtStatistics;
import com.ruoyi.system.response.CountResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface FtStatisticsMapper {

    int myInsert(FtStatistics ftStatistics);

    List<CountResponse> getStatisticsNumByHomeIdsAndType(@Param("homeIds") Set<Long> homeIds,
                                                         @Param("type") Integer type,
                                                         @Param("userIds") Set<Long> userIds,
                                                         @Param("startTime") String startTime,
                                                         @Param("endTime") String endTime);

    List<CountResponse> getOrderNum(@Param("homeIds") Set<Long> homeIds,
                                    @Param("userIds") Set<Long> userIds,
                                    @Param("startTime") String startTime,
                                    @Param("endTime") String endTime);

    Set<Long> getUserIdsByHomeIds(@Param("homeIds") Set<Long> homeIds);

}