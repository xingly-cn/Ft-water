package com.ruoyi.web.controller.admin;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.UserHomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/5/1 15:34
 */
@RestController
@RequestMapping("v1/admin/userHome")
@Api(tags = "用户楼栋管理")
public class UserHomeController extends BaseController {

    @Autowired
    private UserHomeService userHomeService;

    @DeleteMapping("/{userId}/{homeId}")
    @ApiOperation("删除用户楼栋管理")
    public AjaxResult deleteUserHomeByUserIdAndHomeId(@PathVariable(value = "userId") Long userId,
                                                      @PathVariable(value = "homeId") Long homeId)
    {
        return AjaxResult.success(userHomeService.deleteUserHomeByUserIdAndHomeId(userId, homeId));
    }
}
