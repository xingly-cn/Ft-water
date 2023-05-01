package com.ruoyi.system.service;

import com.ruoyi.system.domain.Address;
import com.ruoyi.system.request.AddressRequest;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/20 11:54
 */
public interface AddressService {

    Boolean insertAddress(AddressRequest request);

    Boolean updateAddress(AddressRequest request);

    Boolean deleteAddress(Long id);

    List<Address> getAddressList(AddressRequest request);

    Address getAddressDetail(Long id);

    Boolean updateDefaultAddress(Long id);
}
