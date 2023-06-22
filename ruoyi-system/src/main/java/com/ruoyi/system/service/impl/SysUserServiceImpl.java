package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.FtHome;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.request.OrderRequest;
import com.ruoyi.system.request.UserRequest;
import com.ruoyi.system.request.WechatUserInfo;
import com.ruoyi.system.response.UserResponse;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.utils.CommonUtils;
import com.ruoyi.system.utils.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 用户 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    protected Validator validator;

    @Autowired
    private FtOrderMapper ftOrderMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private FtHomeServiceImpl homeService;

    @Autowired
    private TextMessageMapper textMessageMapper;

    @Resource
    private RedisCache redisCache;

    /**
     * 根据条件分页查询用户列表
     *
     * @param request 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<UserResponse> selectUserList(UserRequest request) {
        List<UserResponse> userResponses = userMapper.selectUserList(request);
        if (!CollectionUtils.isEmpty(userResponses)) {
            List<FtHome> homes = homeService.getHomes();
            userResponses.forEach(userResponse -> {
                if (userResponse.getHomeId() != null) {
                    FtHome home = homeService.getTopHome(homes, userResponse.getHomeId());
                    if (home != null) {
                        userResponse.setSchoolId(home.getId());
                        userResponse.setSchoolName(home.getName());
                    }
                }
            });
        }
        return userResponses;
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<SysUser> selectAllocatedList(SysUser user) {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkUserNameUnique(SysUser user) {
        long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkUserNameUnique(user.getUserName());
        if (StringUtils.isNotNull(info) && info.getUserId() != userId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkPhoneUnique(SysUser user) {
        long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId() != userId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkEmailUnique(SysUser user) {
        long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId() != userId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    @Override
    public void checkUserDataScope(Long userId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            UserRequest user = new UserRequest();
            user.setUserId(userId);
            List<UserResponse> users = SpringUtils.getAopProxy(this).selectUserList(user);
            if (StringUtils.isEmpty(users)) {
                throw new ServiceException("没有权限访问用户数据！");
            }
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user) {
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public boolean registerUser(SysUser user) {
        return userMapper.insertUser(user) > 0;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
//    @CacheEvict(value = "user", key = "#user.userId")
    public int updateUser(SysUser user) {
        // 新增用户与角色管理
        insertUserRole(user);
        //判读是否是自己修改自己的信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser.getUserId().equals(user.getUserId())) {
            //更新缓存用户信息
            loginUser.setUser(user);
            tokenService.setLoginUser(loginUser);
        }
        return userMapper.updateUser(user);
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional
    public void insertUserAuth(Long userId, Long[] roleIds) {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
//    @CacheEvict(value = "user", key = "#user.userId")
    public int updateUserProfile(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password) {
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional
//    @CacheEvict(value = "user", allEntries = true)
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(new SysUser(userId));
            checkUserDataScope(userId);
        }
        // 删除用户与角色关联
        userRoleMapper.deleteUserRole(userIds);
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u)) {
                    BeanValidators.validateWithException(validator, user);
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    userMapper.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账号 ").append(user.getUserName()).append(" 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, user);
                    checkUserAllowed(u);
                    checkUserDataScope(u.getUserId());
                    user.setUserId(u.getUserId());
                    user.setUpdateBy(operName);
                    userMapper.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账号 ").append(user.getUserName()).append(" 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、账号 ").append(user.getUserName()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public ConcurrentHashMap<String, Integer> checkCoupon(HttpServletRequest request) {
        Long userId = SecurityUtils.getUserId();
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(userId);
//        List<OrderResponse> ftOrders = ftOrderMapper.selectList(orderRequest);
//        int totalNum = ftOrders.stream().mapToInt(FtOrder::getNum).sum();
        SysUser user = userMapper.selectUserById(userId);
        Integer waterNum = user.getWaterNum();
        ConcurrentHashMap<String, Integer> result = new ConcurrentHashMap<>();
//        result.put("usedNum", totalNum - waterNum);
        result.put("unUsedNum", waterNum);
        return result;
    }

    @Override
    public String change(UserRequest request) {
        String phone = request.getPhonenumber();
        String password = request.getPassword();
        String newPassword = request.getNewPassword();

        if (StringUtils.isEmpty(phone)) {
            throw new ServiceException("手机号不能为空");
        }

        if (StringUtils.isEmpty(password)) {
            throw new ServiceException("密码不能为空");
        }

        if (StringUtils.isEmpty(newPassword)) {
            throw new ServiceException("新密码不能为空");
        }

        if (password.equals(newPassword)) {
            throw new ServiceException("新密码不能与旧密码相同");
        }

        checkLoginUser(phone, password);
        userMapper.updatePasswordByPhone(newPassword, phone);
        return "修改成功";
    }

    @Override
    @Transactional
    public UserResponse loginUser(UserRequest request) {
        if (StringUtils.isEmpty(request.getCode())) {
            throw new ServiceException("code不能为空");
        }
        String[] codeArray = WechatUtil.getOpenId(request.getCode());
        String openId = codeArray[0];

        String phone = request.getPhonenumber();

        if (StringUtils.isEmpty(phone)) {
            throw new ServiceException("手机号不能为空");
        }

        //根据手机号和openId查询用户 唯一
        SysUser user = userMapper.getUserByOpenId(openId);

        if (user == null) {
            //register
            user = new SysUser();
            BeanUtils.copyProperties(request, user);
            user.setOpenId(openId);
            user.setPhonenumber(phone);
            user.setCreateTime(new Date());
            user.setRoleIds(new Long[]{2L});
            user.setPassword(SecurityUtils.encryptPassword(CommonUtils.getPhoneLast6(phone)));
            insertUser(user);
        }

        if (!user.getPhonenumber().equals(phone)){
            throw new ServiceException("该微信号已经和其它手机号关联吧");
        }

        LoginUser loginUser = new LoginUser();
        loginUser.setUser(user);
        loginUser.setUserId(user.getUserId());
        String token = tokenService.createToken(loginUser);
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        userResponse.setToken(token);
        return userResponse;
    }

    @Override
    public String getPhone(UserRequest request) {
        log.info("getPhone code:{}", request.getCode());
        return WechatUtil.getPhone(request.getCode());
    }

    @Override
    public AjaxResult changeUserPhone(UserRequest request) {
        Long userId = SecurityUtils.getUserId();
        log.info("changeUserPhone userId:{}", userId);
        SysUser sysUser = userMapper.selectUserByPhone(request.getPhonenumber());
        if (sysUser != null) {
            throw new ServiceException("该手机号已被注册");
        }
        SysUser user = userMapper.selectUserById(userId);

        // 从redis校验验证码是否正确，这里先写死，因为甲方还没有购买短信
        String rCode = redisCache.getCacheObject("sms:" + request.getPhonenumber());
        if (rCode != null && rCode.equals(request.getCode())) {
            user.setPhonenumber(request.getPhonenumber());
            userMapper.updatePhoneById(user);
            return userMapper.updatePhoneById(user) > 0 ? AjaxResult.success("修改手机号成功") : AjaxResult.error("修改手机号失败");
        }
        return AjaxResult.error("验证码错误");
    }

    @Override
    public SysUser getUserInfo(HttpServletRequest rq) {
        Long userId = SecurityUtils.getUserId();
        return userMapper.selectUserById(userId);
    }

    @Override
    public SysUser checkPhone(String phone, String code) {
        //通过手机号查询用户
        SysUser user = userMapper.getUserByPhone(phone);

        if (user == null) {
            user = new SysUser();
            user.setPhonenumber(phone);
            return user;
        }

        log.warn("check phone[{}],code[{}]", phone, code);
        if (StringUtils.isNotEmpty(phone)) {
            String[] codeArray = WechatUtil.getOpenId(code);
            String openId = codeArray[0];
            this.userMapper.updateOpenIdByPhone(openId, phone);
        }
        return user;
    }

    @Override
    public Boolean updateUserInfo(WechatUserInfo userInfo) {
        SysUser user = userMapper.getUserByOpenId(userInfo.getOpenId());

        user.setAvatar(userInfo.getAvatar());
        user.setUserName(userInfo.getName());
        user.setAddressId(userInfo.getAddress());
        user.setHomeId(userInfo.getHomeId());
        user.setDormType(userInfo.getDormType());
        user.setSex(String.valueOf(userInfo.getSex()));
        int i = userMapper.updateUser(user);
        return i == 1;
    }

    @Override
    public List<SysUser> search(String keyword) {
        return userMapper.search(keyword);
    }

    private void checkLoginUser(String phone, String password) {
        SysUser user = userMapper.getUserByPhone(phone);

        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        if (!user.getPassword().equals(password)) {
            throw new ServiceException("密码错误");
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds) {
        if (StringUtils.isNotEmpty(roleIds)) {
            //先去查询用户是否已经有角色 有的话过滤 没有的话直接新增
            List<SysUserRole> userRoleList = userRoleMapper.selectUserRoleByUserId(userId);
            if (StringUtils.isNotEmpty(userRoleList)) {
                List<Long> userRoleIdList = userRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
                List<Long> roleIdList = Arrays.asList(roleIds);

                if (new HashSet<>(userRoleIdList).containsAll(roleIdList)) {
                    return;
                }
                List<SysUserRole> list = new ArrayList<>(roleIdList.size());
                for (Long roleId : roleIdList) {
                    SysUserRole ur = new SysUserRole();
                    ur.setUserId(userId);
                    ur.setRoleId(roleId);
                    list.add(ur);
                }
                userRoleMapper.batchUserRole(list);
            } else {
                // 新增用户与角色管理
                List<SysUserRole> list = new ArrayList<SysUserRole>(roleIds.length);
                for (Long roleId : roleIds) {
                    SysUserRole ur = new SysUserRole();
                    ur.setUserId(userId);
                    ur.setRoleId(roleId);
                    list.add(ur);
                }
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    public void updateUserWater(Long userId, int newWaterNum) {
        userMapper.updateUserWater(userId, newWaterNum);
    }

    public void updateUserBarrenNum(Long userId, int newBarrenNum) {
        userMapper.updateUserBarrenNum(userId, newBarrenNum);
    }
}
