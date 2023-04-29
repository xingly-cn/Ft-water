package com.ruoyi.web.controller.admin;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.request.MessageRequest;
import com.ruoyi.system.service.FtMessageService;
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
    public AjaxResult getMessageList(MessageRequest request) {
        return AjaxResult.success(messageService.getMessagePage(request));
    }

    @GetMapping("/detail")
    @ApiOperation("消息详情")
    public AjaxResult getMessageDetail(@RequestParam(value = "id") Long id) {
        return AjaxResult.success(messageService.selectByPrimaryKey(id));
    }

    @PostMapping("/confirm")
    @ApiOperation("确认消息")
    public AjaxResult addMessage(@RequestParam(value = "id") Long id) {
        return AjaxResult.success(messageService.addMessage(id));
    }

}
