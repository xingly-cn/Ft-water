package com.ruoyi.web.controller.admin;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.entity.FtSchool;
import com.ruoyi.system.request.SchoolRequest;
import com.ruoyi.system.response.SchoolResponse;
import com.ruoyi.system.service.FtSchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Desc 学校
 * @Author 方糖
 * @Date 2023-03-30 15:38
 **/
@RestController
@RequestMapping("v1/admin/school")
@Api(tags = "后台-学校")
public class SchoolController extends BaseController {

    @Resource
    private FtSchoolService schoolService;

    @GetMapping("/page")
    @ApiOperation("学校列表-分页")
    public TableDataInfo getSchoolList(SchoolRequest request) {
        startPage();
        List<SchoolResponse> list = schoolService.selectSchoolList(request);
        return getDataTable(list);
    }


    @GetMapping("/detail")
    @ApiOperation("学校详情")
    public AjaxResult getSchoolDetail(@RequestParam(value = "id") Long id) {
        return AjaxResult.success(schoolService.selectByPrimaryKey(id));
    }

    @PostMapping("/insert")
    @ApiOperation("新增学校")
    public AjaxResult addSchool(@RequestBody FtSchool School) {
        return AjaxResult.success(schoolService.addSchool(School));
    }

    @PostMapping("/update")
    @ApiOperation("修改学校")
    public AjaxResult updateSchool(@RequestBody FtSchool School) {
        return AjaxResult.success(schoolService.updateSchool(School));
    }

    @GetMapping("/list")
    @ApiOperation("学校列表")
    public AjaxResult selectSchoolList(SchoolRequest request) {
        return AjaxResult.success(schoolService.selectSchoolList(request));
    }

    @DeleteMapping("/remove")
    @ApiOperation("删除学校")
    public AjaxResult delete(@RequestParam Long id) {
        return AjaxResult.success(schoolService.deleteByPrimaryKey(id));
    }

}
