package com.ruoyi.system.service;

import java.util.Map;

/**
 * @Desc
 * @Author 方糖
 * @Date 2023-06-24 22:52
 **/
public interface FtStatisticsService {
    Map<String, Object> getDashBoardData(String type,String startTime,String endTime);
}
