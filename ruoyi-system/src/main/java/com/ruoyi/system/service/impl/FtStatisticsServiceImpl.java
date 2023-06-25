package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.FtHome;
import com.ruoyi.system.domain.UserHome;
import com.ruoyi.system.mapper.FtOrderMapper;
import com.ruoyi.system.mapper.FtStatisticsMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.response.CountResponse;
import com.ruoyi.system.response.OrderHomeCountResponse;
import com.ruoyi.system.response.SaleCountResponse;
import com.ruoyi.system.service.FtOrderService;
import com.ruoyi.system.service.FtStatisticsService;
import com.ruoyi.system.service.UserHomeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
    private FtHomeServiceImpl homeService;

    @Resource
    private UserHomeService userHomeService;

    @Resource
    private FtOrderService ftOrderService;

    @Resource
    private FtOrderMapper ftOrderMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public Map<String, Object> getDashBoardData(String type,String startTime,String endTime) {
        Map<String, Object> res = new HashMap<>();
        List<SaleCountResponse> saleData = new LinkedList<>();
        Set<OrderHomeCountResponse> orderData = new HashSet<>();
        LinkedHashMap<Integer, Map<Long, Object>> countNumberMap;
        Long userId = SecurityUtils.getUserId();
        List<SaleCountResponse> saleCountResponses = new ArrayList<>();

        switch (type) {
            case "weixin":
                // 查询当前用户的数据
                String userName = SecurityUtils.getUsername();
                List<Long> homeIds = userHomeService.selectHomeIdByUserId(userId);
                if (CollectionUtils.isEmpty(homeIds)) {
                    return res;
                }
                countNumberMap = getCountNumberMap(homeIds,startTime,endTime);
//                saleData.addAll(myMethod(userId, userName,countNumberMap));
                getSaleList(saleData, myMethod(userId, userName, countNumberMap));
                res.put("saleData", saleData);
                res.put("orderData", ftOrderService.homeCount(userId,startTime,endTime));
                break;
            case "pc":
                //权限判断 如果是管理员  查询所有的数据  如果是普通用户  查询当前用户的数据
                List<String> roles = userMapper.selectRoleByUserId(userId);
                boolean flag = false;
                if (CollectionUtils.isNotEmpty(roles)){
                    flag = roles.contains("admin")|| roles.contains("systemAdmin");
                }
                // 查询所有的数据
                List<UserHome> userHomes = userHomeService.selectAllUserHomes(flag,userId);
//                saleData.add(getTemp(Long.valueOf(homeId)));

                if (CollectionUtils.isNotEmpty(userHomes)) {
                    countNumberMap = getCountNumberMap(userHomes.stream().map(UserHome::getHomeId).collect(Collectors.toList()), startTime, endTime);
                    Map<Long, String> userMap = userHomes.stream().collect(Collectors.toMap(UserHome::getUserId, UserHome::getUserName, (k1, k2) -> k1));
                    userMap.forEach((k, v) -> {
                        saleCountResponses.addAll(myMethod(k, v, countNumberMap));
                        List<OrderHomeCountResponse> orderHomeCountResponses = ftOrderService.homeCount(k, startTime, endTime);
                        orderData.addAll(orderHomeCountResponses);
                    });
                    getSaleList(saleData, saleCountResponses);
                }

                res.put("saleData", saleData);
                res.put("orderData", orderData);
                break;
        }
        return res;
    }

    public List<SaleCountResponse> myMethod(Long userId, String userName, LinkedHashMap<Integer, Map<Long, Object>> countNumberMap) {
        List<SaleCountResponse> res = new LinkedList<>();
        List<Long> homeIds = userHomeService.selectHomeIdByUserId(userId);
        if (CollectionUtils.isEmpty(homeIds)) {
            return res;
        }

        homeIds.forEach(id -> {
            SaleCountResponse response = getTemp(id, userId, userName,
                    countNumberMap.get(1).get(id) != null ? (int) countNumberMap.get(1).get(id) : 0,
                    countNumberMap.get(2).get(id) != null ? (int) countNumberMap.get(2).get(id) : 0,
                    countNumberMap.get(3).get(id) != null ? (int) countNumberMap.get(3).get(id) : 0,
                    countNumberMap.get(4).get(id) != null ? (int) countNumberMap.get(4).get(id) : 0,
                    countNumberMap.get(5).get(id) != null ? (BigDecimal) countNumberMap.get(5).get(id) : BigDecimal.ZERO);
            res.add(response);
        });
        return res;
    }

    public SaleCountResponse getTemp(Long homeId, Long userId, String userName, Integer orderNum, Integer couponNum, Integer waterNum, Integer bottomNum, BigDecimal totalPrice) {

        // 递归获取完整楼栋名称
        FtHome home = homeService.selectByPrimaryKey(homeId);
        Long parentId = home.getParentId();
        String homeName = "";
        if (parentId != 0) { // 有父级地址
            FtHome parentHome = homeService.selectByPrimaryKey(parentId);
            homeName += parentHome.getName() + "/";
        }
        homeName += home.getName();

        return SaleCountResponse.builder()
                .homeId(homeId)
                .homeName(homeName)
                .userId(userId)
                .userName(userName)
                .orderNum(orderNum)
                .totalPrice(totalPrice)
                .couponNum(couponNum)
                .waterNum(waterNum)
                .bottomNum(bottomNum)
                .build();
    }

    private Map<Long, Object> assembleMap(List<CountResponse> waitHandleList, Integer type) {
        if (CollectionUtils.isEmpty(waitHandleList)) {
            return new HashMap<>();
        }
        if (type == 1) {
            return waitHandleList.stream().collect(Collectors.toMap(CountResponse::getHomeId, CountResponse::getNumber));
        }
        return waitHandleList.stream().collect(Collectors.toMap(CountResponse::getHomeId, CountResponse::getPrice));
    }


    private LinkedHashMap<Integer, Map<Long, Object>> getCountNumberMap(List<Long> homeIds,String startTime,String endTime) {
        List<CountResponse> orderList = ftStatisticsMapper.getOrderNum(homeIds,startTime,endTime);
        Map<Long, Object> orderMap = assembleMap(orderList, 1);
        List<CountResponse> CouponList = ftStatisticsMapper.getStatisticsNumByHomeIdsAndType(homeIds, 0,startTime,endTime);
        Map<Long, Object> couponMap = assembleMap(CouponList, 1);
        List<CountResponse> WaterList = ftStatisticsMapper.getStatisticsNumByHomeIdsAndType(homeIds, 1,startTime,endTime);
        Map<Long, Object> waterMap = assembleMap(WaterList, 1);
        List<CountResponse> BottomList = ftStatisticsMapper.getStatisticsNumByHomeIdsAndType(homeIds, 2,startTime,endTime);
        Map<Long, Object> bottomMap = assembleMap(BottomList, 1);
        List<CountResponse> orderPriceList = ftOrderMapper.getPriceByHomeIds(homeIds,startTime,endTime);
        Map<Long, Object> orderPriceMap = assembleMap(orderPriceList, 2);
        return new LinkedHashMap<Integer,Map<Long, Object>>(5) {
            {
                put(1, orderMap);
                put(2, couponMap);
                put(3, waterMap);
                put(4, bottomMap);
                put(5, orderPriceMap);
            }
        };
    }

    private void getSaleList(List<SaleCountResponse> saleData,List<SaleCountResponse> saleCountResponses){
        if (CollectionUtils.isNotEmpty(saleCountResponses)) {
            Map<Long, List<SaleCountResponse>> saleCountHomeMap = saleCountResponses.stream().collect(Collectors.groupingBy(SaleCountResponse::getHomeId));
            Set<Long> keys = saleCountHomeMap.keySet();
            keys.forEach(k->{
                SaleCountResponse response = new SaleCountResponse();
                BeanUtils.copyProperties(saleCountHomeMap.get(k).get(0),response);
                response.setUserId(null);
                response.setUserName(null);
                response.setUsers(saleCountHomeMap.get(k));
                saleData.add(response);
            });
        }
    }

}
