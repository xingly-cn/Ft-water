package com.ruoyi.system.service;


import com.ruoyi.system.domain.FtHome;
import com.ruoyi.system.request.HomeRequest;
import com.ruoyi.system.response.HomeResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2023/4/18 21:41
 */
public interface FtHomeService {

    List<HomeResponse> homeTree();

    Boolean deleteByPrimaryKey(Long id);

    Boolean addHome(FtHome record);

    Boolean updateHome(FtHome record);

    Boolean addNumber(HomeRequest request);

    HomeResponse selectByPrimaryKey(Long id);

    Boolean addUser(HomeRequest request);

    Boolean addNumberByHomeId(Long homeId, Integer number);

    List<HomeResponse> homeList(HomeRequest request);

    Map<String,Integer> count(Long homeId,Long userId);
}
