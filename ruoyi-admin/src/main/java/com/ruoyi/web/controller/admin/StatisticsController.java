package com.ruoyi.web.controller.admin;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.FtStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Desc
 * @Author 方糖
 * @Date 2023-06-24 21:55
 **/
@RestController
@RequestMapping("v1/admin/statistics")
@Api(tags = "数据统计")
public class StatisticsController {

    @Resource
    private FtStatisticsService ftStatisticsService;

    @GetMapping("show")
    @ApiOperation("数据大屏")
    public AjaxResult data(String type) {
        return AjaxResult.success(ftStatisticsService.getDashBoardData(type));
    }


}
