package com.ruoyi.web.controller.admin;


import com.ruoyi.system.request.SchoolRequest;
import com.ruoyi.system.entity.FtSchool;
import com.ruoyi.system.service.FtSchoolService;
import com.ruoyi.system.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Desc 学校
 * @Author 方糖
 * @Date 2023-03-30 15:38
 **/
@RestController
@RequestMapping("v1/admin/school")
@Api(tags = "后台-学校")
public class SchoolController {

    @Resource
    private FtSchoolService schoolService;

    @GetMapping("/page")
    @ApiOperation("学校列表-分页")
    public Result<?> getSchoolList(SchoolRequest request) {
        return Result.success(schoolService.getSchoolPage(request));
    }

    @GetMapping("/detail")
    @ApiOperation("学校详情")
    public Result<?> getSchoolDetail(@RequestParam(value = "id") Long id) {
        return Result.success(schoolService.selectByPrimaryKey(id));
    }

    @PostMapping("/insert")
    @ApiOperation("新增学校")
    public Result<?> addSchool(@RequestBody FtSchool School) {
        return Result.success(schoolService.addSchool(School));
    }

    @PostMapping("/update")
    @ApiOperation("修改学校")
    public Result<?> updateSchool(@RequestBody FtSchool School) {
        return Result.success(schoolService.updateSchool(School));
    }

    @GetMapping("/list")
    @ApiOperation("学校列表")
    public Result<?> selectSchoolList(FtSchool School) {
        return Result.success(schoolService.selectSchoolList(School));
    }

    @DeleteMapping("/remove")
    @ApiOperation("删除学校")
    public Result<?> delete(@RequestParam Long id) {
        return Result.success(schoolService.deleteByPrimaryKey(id));
    }

}
