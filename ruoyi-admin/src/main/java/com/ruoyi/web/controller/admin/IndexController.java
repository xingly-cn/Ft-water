package com.ruoyi.web.controller.admin;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.request.IndexRequest;
import com.ruoyi.system.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/31 22:37
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @PostMapping("/countEveryDay")
    public AjaxResult countEveryDay(@RequestBody IndexRequest request) {
        return AjaxResult.success(indexService.countEveryDay(request));
    }
}
