package com.ruoyi.web.controller.admin;

import com.asugar.ftwaterdelivery.entity.Config;
import com.asugar.ftwaterdelivery.service.ConfigService;
import com.asugar.ftwaterdelivery.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (config)表控制层
 *
 * @author xxxxx
 */
@RestController
@RequestMapping("/config")
@Api(tags = "参数配置")
public class ConfigController {
    /**
     * 服务对象
     */
    @Resource
    private ConfigService configService;

    @GetMapping("/page")
    @ApiOperation("参数列表-分页")
    public Result<?> getConfigList(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                   @RequestParam(value = "size",defaultValue = "10")Integer size)
    {
        return Result.success(configService.getConfigList(page,size));
    }

    @GetMapping("/detail")
    @ApiOperation("参数详情")
    public Result<?> getConfigDetail(@RequestParam(value = "id")Long id)
    {
        return Result.success(configService.selectByPrimaryKey(id));
    }

    @PostMapping("/insert")
    @ApiOperation("参数详情")
    public Result<?> addConfig(@RequestBody Config config)
    {
        return Result.success(configService.addConfig(config));
    }

    @PostMapping("/update")
    @ApiOperation("参数详情")
    public Result<?> updateConfig(@RequestBody Config config)
    {
        return Result.success(configService.updateConfig(config));
    }

    @GetMapping("/list")
    @ApiOperation("参数列表")
    public Result<?> selectConfigList(Config config)
    {
        return Result.success(configService.selectConfigList(config));
    }
}
