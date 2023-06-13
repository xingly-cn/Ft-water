package com.ruoyi.web.controller.admin;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.Address;
import com.ruoyi.system.request.AddressRequest;
import com.ruoyi.system.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/20 11:52
 */
@Api(tags = "地址管理")
@RestController
@RequestMapping("v1/admin/address")
public class AddressController extends BaseController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/insert")
    @ApiOperation("新增地址")
    public AjaxResult insertAddress(@RequestBody AddressRequest request)
    {
        return AjaxResult.success(addressService.insertAddress(request));
    }

    @PostMapping("/update")
    @ApiOperation("修改地址")
    public AjaxResult updateAddress(@RequestBody AddressRequest request)
    {
        return AjaxResult.success(addressService.updateAddress(request));
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除地址")
    public AjaxResult deleteAddress(@RequestParam(value = "id")Long id)
    {
        return AjaxResult.success(addressService.deleteAddress(id));
    }

    @GetMapping("/list")
    @ApiOperation("获取当前用户地址列表")
    public TableDataInfo getAddressList(AddressRequest request)
    {
        startPage();
        List<Address> addressList = addressService.getAddressList(request);
        return getDataTable(addressList);
    }

    @GetMapping("/detail")
    @ApiOperation("地址详情")
    public AjaxResult getAddressDetail(Long id)
    {
        return AjaxResult.success(addressService.getAddressDetail(id));
    }

    @PostMapping("/default")
    @ApiOperation("设置为默认地址")
    public AjaxResult   updateDefaultAddress(@RequestBody AddressRequest request)
    {
        return AjaxResult.success(addressService.updateDefaultAddress(request.getId()));
    }
}
