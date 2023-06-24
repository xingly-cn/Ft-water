package com.ruoyi.system.mapper;


import com.ruoyi.system.domain.FtStatistics;

public interface FtStatisticsMapper {

    int myInsert(FtStatistics ftStatistics);

    Integer getOrderNum(Long homeId);
    Integer getCouponNum(Long homeId);
    Integer getWaterNum(Long homeId);
    Integer getBottomNum(Long homeId);


}