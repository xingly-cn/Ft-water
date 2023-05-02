package com.ruoyi.web.controller.admin;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.request.MessageRequest;
import com.ruoyi.system.response.MessageResponse;
import com.ruoyi.system.service.FtMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/19 22:49
 */
@RestController
@RequestMapping("v1/admin/message")
@Api(tags = "消息")
public class MessageController extends BaseController {

    @Autowired
    private FtMessageService messageService;

    @GetMapping("/page")
    @ApiOperation("消息列表-分页")
    public TableDataInfo getMessageList(MessageRequest request) {
        startPage();
        List<MessageResponse> responses = messageService.getMessageList(request);
        return getDataTable(responses);
    }

    @GetMapping("/detail")
    @ApiOperation("消息详情")
    public AjaxResult getMessageDetail(@RequestParam(value = "id") Long id) {
        return AjaxResult.success(messageService.selectByPrimaryKey(id));
    }

    @PostMapping("/confirm")
    @ApiOperation("确认消息")
    public AjaxResult confirmMessage(@RequestParam(value = "id") Long id) {
        return AjaxResult.success(messageService.confirmMessage(id));
    }

}
