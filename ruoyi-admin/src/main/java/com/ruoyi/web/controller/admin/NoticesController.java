package com.ruoyi.web.controller.admin;


import com.ruoyi.web.controller.request.NoticesRequest;
import com.ruoyi.web.service.FtNoticesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/19 22:49
 */
@RestController
@RequestMapping("v1/admin/notices")
@Api(tags = "通知")
public class NoticesController {

    @Autowired
    private FtNoticesService noticesService;

    @GetMapping("/page")
    @ApiOperation("通知列表-分页")
    public com.asugar.ftwaterdelivery.utils.Result<?> getNoticesList(NoticesRequest request) {
        return com.asugar.ftwaterdelivery.utils.Result.success(noticesService.getNoticesPage(request));
    }

    @GetMapping("/detail")
    @ApiOperation("通知详情")
    public com.asugar.ftwaterdelivery.utils.Result<?> getNoticesDetail(@RequestParam(value = "id") Long id) {
        return com.asugar.ftwaterdelivery.utils.Result.success(noticesService.selectByPrimaryKey(id));
    }
}
