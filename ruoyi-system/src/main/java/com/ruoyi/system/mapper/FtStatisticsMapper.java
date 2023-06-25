package com.ruoyi.system.mapper;


import com.ruoyi.system.domain.FtStatistics;
import com.ruoyi.system.response.CountResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FtStatisticsMapper {

    int myInsert(FtStatistics ftStatistics);

    List<CountResponse> getStatisticsNumByHomeIdsAndType(@Param("homeIds") List<Long> homeIds,
                                                         @Param("type") Integer type,
                                                         @Param("startTime") String startTime,
                                                         @Param("endTime") String endTime);

    List<CountResponse> getOrderNum(@Param("homeIds") List<Long> homeIds,
                                    @Param("startTime") String startTime,
                                    @Param("endTime") String endTime);
}