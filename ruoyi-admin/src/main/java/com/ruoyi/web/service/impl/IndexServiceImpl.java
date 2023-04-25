package com.ruoyi.web.service.impl;


import com.ruoyi.web.controller.request.IndexRequest;
import com.ruoyi.web.controller.response.IndexCountResponse;
import com.ruoyi.web.dao.FtSchoolMapper;
import com.ruoyi.web.service.IndexService;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/31 22:39
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private FtSchoolMapper schoolMapper;

    @Override
    public List<IndexCountResponse> countEveryDay(IndexRequest request) {
        //查看各学校 每天的销售量、月销售量、全部学校当天总销售量、月总销售量
        if (request.getChoose()== null){
            throw new ServiceException("请选择统计方式");
        }
        if (CollectionUtils.isEmpty(request.getSchoolIds())){
            throw new ServiceException("请选择学校");
        }
        List<com.asugar.ftwaterdelivery.controller.response.IndexCountResponse> indexCountResponses = Lists.newArrayList();
        String time = "";
        if (request.getChoose() == 0){
            time = utils.DateUtils.getCurrentDate("yyyy-MM-dd");
            indexCountResponses = initDayCount();
        }else if (request.getChoose() == 1){
            time = com.asugar.ftwaterdelivery.utils.DateUtils.getCurrentDate("yyyy-MM");
            indexCountResponses = initMonthCount();
        }

        List<com.asugar.ftwaterdelivery.controller.response.IndexCountResponse> countIndex = schoolMapper.countIndex(time, request.getSchoolIds());
        if (CollectionUtils.isEmpty(countIndex)){
            return indexCountResponses;
        }

        for (com.asugar.ftwaterdelivery.controller.response.IndexCountResponse indexCountResponse : indexCountResponses) {
            for (com.asugar.ftwaterdelivery.controller.response.IndexCountResponse countIndexResponse : countIndex) {
                indexCountResponse.setSchoolId(countIndexResponse.getSchoolId());
                indexCountResponse.setSchoolName(countIndexResponse.getSchoolName());
                if (indexCountResponse.getTime().equals(countIndexResponse.getTime())){
                    indexCountResponse.setCount(countIndexResponse.getCount());
                }
            }
        }
        return indexCountResponses;
    }


    private List<com.asugar.ftwaterdelivery.controller.response.IndexCountResponse> initMonthCount() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        List<String> monthFullDay = com.asugar.ftwaterdelivery.utils.DateUtils.getMonthFullDay(year, month);
        List<com.asugar.ftwaterdelivery.controller.response.IndexCountResponse> countResponses = Lists.newArrayList();
        monthFullDay.forEach(day -> {
            countResponses.add(com.asugar.ftwaterdelivery.controller.response.IndexCountResponse.builder()
                    .time(day)
                    .count(0)
                    .build()
            );
        });
        return countResponses;
    }

    private List<com.asugar.ftwaterdelivery.controller.response.IndexCountResponse> initDayCount() {
        String dayFullHour = com.asugar.ftwaterdelivery.utils.DateUtils.getCurrentDate();
        return Lists.newArrayList(com.asugar.ftwaterdelivery.controller.response.IndexCountResponse.builder()
                .time(dayFullHour)
                .count(0)
                .build());
    }

}
