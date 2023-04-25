package com.ruoyi.web.controller.admin;

import com.asugar.ftwaterdelivery.controller.request.MessageRequest;
import com.asugar.ftwaterdelivery.service.FtMessageService;
import com.asugar.ftwaterdelivery.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/19 22:49
 */
@RestController
@RequestMapping("v1/admin/message")
@Api(tags = "消息")
public class MessageController {

    @Autowired
    private FtMessageService messageService;

    @GetMapping("/page")
    @ApiOperation("消息列表-分页")
    public Result<?> getMessageList(MessageRequest request) {
        return Result.success(messageService.getMessagePage(request));
    }

    @GetMapping("/detail")
    @ApiOperation("消息详情")
    public Result<?> getMessageDetail(@RequestParam(value = "id") Long id) {
        return Result.success(messageService.selectByPrimaryKey(id));
    }

    @PostMapping("/confirm")
    @ApiOperation("确认消息")
    public Result<?> addMessage(@RequestParam(value = "id") Long id) {
        return Result.success(messageService.addMessage(id));
    }

}
