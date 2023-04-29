package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.mapper.FtHomeMapper;
import com.ruoyi.system.request.IndexRequest;
import com.ruoyi.system.response.IndexCountResponse;
import com.ruoyi.system.service.IndexService;
import com.ruoyi.system.utils.DateUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    private FtHomeMapper homeMapper;

    @Override
    public List<IndexCountResponse> countEveryDay(IndexRequest request) {
        //查看各学校 每天的销售量、月销售量、全部学校当天总销售量、月总销售量
        if (request.getChoose() == null) {
            throw new ServiceException("请选择统计方式");
        }
        if (CollectionUtils.isEmpty(request.getSchoolIds())) {
            throw new ServiceException("请选择学校");
        }
        List<IndexCountResponse> indexCountResponses = Lists.newArrayList();
        String time = "";
        if (request.getChoose() == 0) {
            time = DateUtils.getCurrentDate("yyyy-MM-dd");
            indexCountResponses = initDayCount();
        } else if (request.getChoose() == 1) {
            time = DateUtils.getCurrentDate("yyyy-MM");
            indexCountResponses = initMonthCount();
        }

        List<IndexCountResponse> countIndex = homeMapper.countIndex(time, request.getSchoolIds());
        if (CollectionUtils.isEmpty(countIndex)) {
            return indexCountResponses;
        }

        for (IndexCountResponse indexCountResponse : indexCountResponses) {
            for (IndexCountResponse countIndexResponse : countIndex) {
                indexCountResponse.setSchoolId(countIndexResponse.getSchoolId());
                indexCountResponse.setSchoolName(countIndexResponse.getSchoolName());
                if (indexCountResponse.getTime().equals(countIndexResponse.getTime())) {
                    indexCountResponse.setCount(countIndexResponse.getCount());
                }
            }
        }
        return indexCountResponses;
    }


    private List<IndexCountResponse> initMonthCount() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        List<String> monthFullDay = DateUtils.getMonthFullDay(year, month);
        List<IndexCountResponse> countResponses = Lists.newArrayList();
        monthFullDay.forEach(day -> {
            countResponses.add(IndexCountResponse.builder()
                    .time(day)
                    .count(0)
                    .build()
            );
        });
        return countResponses;
    }

    private List<IndexCountResponse> initDayCount() {
        String dayFullHour = DateUtils.getCurrentDate();
        List<IndexCountResponse> response = Lists.newArrayList();
        response.add(IndexCountResponse.builder()
                .time(dayFullHour)
                .count(0)
                .build());
        return response;
    }

}
