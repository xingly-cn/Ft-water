package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Address;
import com.ruoyi.system.exception.ServiceException;
import com.ruoyi.system.mapper.AddressMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.request.AddressRequest;
import com.ruoyi.system.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/19 22:03
 */
@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressMapper addressMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Override
    @Transactional
    public Boolean insertAddress(AddressRequest request) {
        log.info("insertAddress request[{}]", request);
        Long userId = SecurityUtils.getUserId();
        request.setUserId(userId);
        //校验手机号
        if (StringUtils.isNotEmpty(request.getPhone()) && request.getPhone().length() > 11) {
            throw new ServiceException(1000, "请输入正确的手机号");
        }
        return addressMapper.insert(request) > 0;
    }

    @Override
    @Transactional
    public Boolean updateAddress(AddressRequest request) {
        log.info("updateAddress request[{}]", request);
        //校验
        return updateByPrimaryKeySelective(request) > 0;
    }

    @Override
    @Transactional
    public Boolean deleteAddress(Long id) {
        log.info("deleteAddress id[{}]", id);
        return addressMapper.deleteAddressById(id) > 0;
    }

    @Override
    public List<Address> getAddressList(AddressRequest request) {
        log.info("getAddressList request[{}]", request);
        //获取当前用户的地址列表
        request.setUserId(SecurityUtils.getUserId());
        return addressMapper.getAddressList(request);
    }

    @Override
    public Address getAddressDetail(Long id) {
        return addressMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public Boolean updateDefaultAddress(Long id) {
        //所有的address 该用户的
        List<Address> addresses = addressMapper.selectAddressesByUserId(SecurityUtils.getUserId());
        //修改其他为非默认地址
        addresses = addresses.stream().filter(address -> !address.getId().equals(id)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(addresses)) {
            addressMapper.updateNoDefaultAddressByIds(addresses.stream().map(Address::getId).collect(Collectors.toList()));
        }
        //更新用户·默认地址
        userMapper.updateDefaultAddressById(SecurityUtils.getUserId(), id);
        return addressMapper.updateDefaultAddressById(id) > 0;
    }

    public int updateByPrimaryKeySelective(Address record) {
        return addressMapper.updateByPrimaryKeySelective(record);
    }
}
