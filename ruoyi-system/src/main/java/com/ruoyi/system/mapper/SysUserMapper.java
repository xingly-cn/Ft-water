package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.request.UserRequest;
import com.ruoyi.system.response.UserResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 用户表 数据层
 *
 * @author ruoyi
 */
public interface SysUserMapper {

    List<Integer> selectAllUser();

    /**
     * 根据条件分页查询用户列表
     *
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
    List<UserResponse> selectUserList(UserRequest request);

    /**
     * 根据条件分页查询已配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<SysUser> selectAllocatedList(SysUser user);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<SysUser> selectUnallocatedList(SysUser user);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUser selectUserByUserName(String userName);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    SysUser selectUserById(Long userId);

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int insertUser(SysUser user);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int updateUser(SysUser user);

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    int deleteUserByIds(Long[] userIds);

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    SysUser checkUserNameUnique(String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @return 结果
     */
    SysUser checkPhoneUnique(String phonenumber);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    SysUser checkEmailUnique(String email);

    int updatePasswordByPhone(@Param("password") String password,
                              @Param("phone") String phone);

    SysUser getUserByPhone(String phone);

    void updateOpenIdByPhone(@Param("openId") String openId,
                             @Param("phone") String phone);

    int updatePhoneById(SysUser user);

    SysUser getUserByOpenId(String openId);

    List<SysUser> getUsersFindByRoleId(int roleId);

    List<SysUser> getUserByHomeIdAndRoleId(@Param("homeId") Long homeId,
                                           @Param("roleId") int roleId);

    List<SysUser> getUsersFindByRoleIdAndUserId(@Param("userId") Long userId,
                                                @Param("roleId") int roleId);

    List<SysUser> search(String keyword);

    void updateDefaultAddressById(@Param("userId") Long userId,
                                  @Param("id") Long id);

    List<SysUser> selectUserByIds(@Param("userIds") Set<Long> userIds);

    Boolean updateWaterNumberById(@Param("id") Long id,
                                  @Param("number") int number);

    Boolean updateBarrelNumberById(@Param("id") Long id,
                                   @Param("number") int number);

    List<SysUser> selectUsersByIds(@Param("ids") List<String> userIds);

    void updateUserWater(@Param("userId") Long userId,@Param("newWaterNum") int newWaterNum);

    void updateUserBarrenNum(@Param("userId")Long userId, @Param("barrelNum") int newBarrenNum);

    SysUser selectUserByPhone(@Param("phone") String phoneNumber);
}
