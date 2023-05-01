package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.Address;
import com.ruoyi.system.request.AddressRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
public interface AddressMapper{

    int insert(Address address);

    int insertSelective(Address record);

    Address selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);

    int deleteAddressById(@Param("id")Long id);

    List<Address> getAddressList(AddressRequest request);

    List<Address> selectAddressesByUserId(@Param("userId")Long userId);

    int updateDefaultAddressById(@Param("id")Long id);

    int updateNoDefaultAddressByIds(@Param("ids")List<Long> ids);

    List<Address> getAddressesByUserIds(@Param("userIds") List<String> userIds);
}