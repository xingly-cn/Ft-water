package com.ruoyi.web.controller.admin;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.request.NoticesRequest;
import com.ruoyi.system.service.FtNoticesService;
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
@Api(tags = "库存变化表")
public class NoticesController {

    @Autowired
    private FtNoticesService noticesService;

    @GetMapping("/page")
    @ApiOperation("库存记录列表-分页")
    public AjaxResult getNoticesList(NoticesRequest request) {
        return AjaxResult.success(noticesService.getNoticesPage(request));
    }

    @GetMapping("/input")
    @ApiOperation("入库查询")
    public AjaxResult getNoticesByInput(long cur, long size) {
        return AjaxResult.success(noticesService.getNoticesByInput(cur, size));
    }

    @GetMapping("/detail")
    @ApiOperation("库存详情")
    public AjaxResult getNoticesDetail(@RequestParam(value = "id") Long id) {
        return AjaxResult.success(noticesService.selectByPrimaryKey(id));
    }
}
