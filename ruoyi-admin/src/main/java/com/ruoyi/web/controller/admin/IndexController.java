package com.ruoyi.web.controller.admin;

import com.asugar.ftwaterdelivery.controller.request.IndexRequest;
import com.asugar.ftwaterdelivery.service.IndexService;
import com.asugar.ftwaterdelivery.utils.Result;
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
    public Result<?> countEveryDay(@RequestBody IndexRequest request) {
        return Result.success(indexService.countEveryDay(request));
    }
}
