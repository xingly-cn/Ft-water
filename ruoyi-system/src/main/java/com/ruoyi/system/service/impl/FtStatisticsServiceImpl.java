package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.FtHome;
import com.ruoyi.system.mapper.FtHomeMapper;
import com.ruoyi.system.mapper.FtOrderMapper;
import com.ruoyi.system.mapper.FtStatisticsMapper;
import com.ruoyi.system.response.OrderHomeCountResponse;
import com.ruoyi.system.service.FtOrderService;
import com.ruoyi.system.service.FtStatisticsService;
import com.ruoyi.system.service.UserHomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Desc
 * @Author 方糖
 * @Date 2023-06-24 22:53
 **/
@Service
@Slf4j
public class FtStatisticsServiceImpl implements FtStatisticsService {

    @Resource
    private FtStatisticsMapper ftStatisticsMapper;

    @Resource
    private FtHomeMapper ftHomeMapper;

    @Resource
    private UserHomeService userHomeService;

    @Resource
    private FtOrderService ftOrderService;

    @Resource
    private FtOrderMapper ftOrderMapper;

    @Override
    public Map<String, Object> getDashBoardData(String type) {
        Map<String, Object> res = new HashMap<>();
        List<Object> saleData = new LinkedList<>();
        Set<OrderHomeCountResponse> orderData = new HashSet<>();

        switch (type) {
            case "weixin":
                // 查询当前用户的数据
                Long userId = SecurityUtils.getUserId();
                saleData.add(myMethod(userId));
                res.put("saleData", saleData);
                res.put("orderData", ftOrderService.homeCount(userId));
                break;
            case "pc":
                // 查询所有的数据
                List<Integer> homeIdList = userHomeService.selectAllHomeId();
                for (Integer homeId : homeIdList) {
                    saleData.add(getTemp(Long.valueOf(homeId)));
                }
                List<Integer> userIdList = userHomeService.selectAllUserId();

                for (Integer uid : userIdList) {
                    List<OrderHomeCountResponse> orderHomeCountResponses = ftOrderService.homeCount(Long.valueOf(uid));
                    orderData.addAll(orderHomeCountResponses);
                }
                res.put("saleData", saleData);
                res.put("orderData", orderData);
                break;
        }
        return res;
    }

    public List<Object> myMethod(Long userId) {
        List<Object> res = new LinkedList<>();
        List<Integer> homeIdList = userHomeService.selectHomeIdByUserId(userId);
        for (Integer homeId : homeIdList) {
            Map<Object, Object> temp = getTemp(Long.valueOf(homeId));
            res.add(temp);
        }
        return res;
    }

    public Map<Object, Object> getTemp (Long homeId) {
        Map<Object, Object> temp = new HashMap<>();
        Integer orderNum = ftStatisticsMapper.getOrderNum(homeId);
        Integer couponNum = ftStatisticsMapper.getCouponNum(homeId);
        Integer waterNum = ftStatisticsMapper.getWaterNum(homeId);
        Integer bottomNum = ftStatisticsMapper.getBottomNum(homeId);
        BigDecimal totalPrice = ftOrderMapper.getPriceByHomeId(homeId);


        if (orderNum == null) {
            orderNum = 0;
        }
        if (couponNum == null) {
            couponNum = 0;
        }
        if (waterNum == null) {
            waterNum = 0;
        }
        if (bottomNum == null) {
            bottomNum = 0;
        }
        if (totalPrice == null) {
            totalPrice = BigDecimal.ZERO;
        }

        // 递归获取完整楼栋名称
        FtHome home = ftHomeMapper.selectFtHomeByHomeId(homeId);
        Long parentId = home.getParentId();
        String homeName = "";
        if (parentId != 0) { // 有父级地址
            FtHome parentHome = ftHomeMapper.selectByPrimaryKey(parentId);
            homeName += parentHome.getName() + "/";
        }
        homeName += home.getName();

        temp.put("orderNum", orderNum);
        temp.put("homeId", homeId);
        temp.put("couponNum", couponNum);
        temp.put("waterNum", waterNum);
        temp.put("bottomNum", bottomNum);
        temp.put("homeName", homeName);
        temp.put("totalPrice", totalPrice);
        return temp;
    }

}
