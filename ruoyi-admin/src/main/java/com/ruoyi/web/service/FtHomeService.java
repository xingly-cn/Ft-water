package com.ruoyi.web.service;


import com.ruoyi.web.controller.request.HomeRequest;
import com.ruoyi.web.controller.response.HomeResponse;
import com.ruoyi.web.entity.FtHome;

import java.util.List;

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
}
