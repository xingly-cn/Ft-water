package com.ruoyi.web.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.web.controller.request.LoginRequest;
import com.ruoyi.web.controller.request.UserRequest;
import com.ruoyi.web.controller.request.WechatUserInfo;
import com.ruoyi.web.controller.response.UserResponse;
import com.ruoyi.web.dao.FtUserMapper;
import com.ruoyi.web.entity.FtUser;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.service.FtUserService;
import com.ruoyi.web.utils.JwtUtils;
import com.ruoyi.web.utils.WechatDecryptDataUtil;
import com.ruoyi.web.utils.WechatUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/30 15:59
 */
@Service
@Slf4j
public class FtUserServiceImpl extends BaseMapperImpl<FtUser, UserResponse, UserRequest, FtUserMapper> implements FtUserService {

    @Resource
    private FtUserMapper ftUserMapper;

    @Override
    @CacheEvict(value = "user", key = "#id")
    public Boolean deleteByPrimaryKey(Long id) {
        return ftUserMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    @CachePut(value = "user", key = "#record.id")
    public Boolean addUser(FtUser record) {
        record.setCreateTime(LocalDateTime.now());
        return ftUserMapper.insert(record) > 0;
    }

    @Override
    @Cacheable(value = "user", key = "#id")
    public FtUser selectByPrimaryKey(Long id) {
        return ftUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean updateUser(FtUser record) {
        return ftUserMapper.updateByPrimaryKeySelective(record) > 0;
    }

    @Override
    public Map<String, Object> getUserPage(UserRequest request) {
        IPage<UserResponse> page = new Page<>(request.getPage(), request.getSize());
        return selectPage(page, request);
    }

    @Override
    public List<FtUser> selectUserList(FtUser User) {
        return ftUserMapper.selectList(new QueryWrapper<>(User));
    }

    @Override
    public UserResponse login(LoginRequest request) {
        String phone = request.getPhone();
        String password = request.getPassword();

        if (StringUtils.isEmpty(phone)) {
            throw new ServiceException("手机号不能为空");
        }

        if (StringUtils.isEmpty(password)) {
            throw new ServiceException("密码不能为空");
        }

        FtUser user = checkLoginUser(phone, password);

        String token = JwtUtils.createToken(user.getOpenId(), user.getHomeId(), String.valueOf(user.getUserType()));
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        userResponse.setToken(token);
        return userResponse;
    }

    @Override
    public UserResponse loginUser(LoginRequest request) {
        if (StringUtils.isEmpty(request.getCode())) {
            throw new ServiceException("code不能为空");
        }
        String[] codeArray = WechatUtil.getOpenId(request.getCode());
        String openId = codeArray[0];
        String sessionKey = codeArray[1];

        String phone = request.getPhone();

        if (StringUtils.isEmpty(phone)) {
            throw new ServiceException("手机号不能为空");
        }

        //根据手机号和openId查询用户 唯一
        FtUser user = ftUserMapper.selectOne(new QueryWrapper<FtUser>().eq("open_id", openId));

        if (user == null) {
            //register
            user = new FtUser();
            user.setOpenId(openId);
            user.setPhone(phone);
            user.setUserType("0");
            user.setDormType(request.getDormType());
            user.setAvatar(request.getAvatar());
            user.setCreateTime(LocalDateTime.now());
            user.setHomeId(user.getHomeId());
            user.setHomeId(request.getHomeId());
            user.setSex(request.getSex());
            user.setName(request.getName());
            user.setAddress(request.getAddress());

            addUser(user);
        }

        String token = JwtUtils.createToken(user.getOpenId(), user.getHomeId(), String.valueOf(user.getUserType()));
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        userResponse.setToken(token);
//        userResponse.setSessionKey(sessionKey);
        return userResponse;
    }

    @Override
    public String decryptPhone(String encryptedData, String iv, String sessionKey) {

        if (StringUtils.isEmpty(sessionKey)) {
            throw new ServiceException("sessionKey不能为空");
        }
        log.info("解密手机:{}, iv:{}, sessionKey:{}", encryptedData, iv, sessionKey);
        String phone = WechatDecryptDataUtil.decryptData(encryptedData, sessionKey, iv);
        phone = JSON.parseObject(phone).getString("phoneNumber");
        log.info("解密手机:{}", phone);
        return phone;
    }

    @Override
    public String getPhone(UserRequest request) {
        log.info("getPhone code:{}", request.getCode());
        return WechatUtil.getPhone(request.getCode());
    }

    @Override
    public String changeUserPhone(UserRequest request) {
        // 由注解已经做了校验, 所以执行到这里, token一定是有效的
        String userId = getCurrentUser().get("userId");
        log.info(userId);
        FtUser user = ftUserMapper.selectOne(new QueryWrapper<FtUser>()
                .eq("open_id", userId));

        // 从redis校验验证码是否正确，这里先写死，因为甲方还没有购买短信
        if ("666666".equals(request.getCode())) {
            user.setPhone(request.getPhone());
            ftUserMapper.updateById(user);
            return "修改手机号成功";
        }
        return "验证码错误";
    }

    @Override
    public FtUser getUserInfo(HttpServletRequest rq) {
        String userId = getCurrentUser().get("userId");
        return ftUserMapper.selectOne(new QueryWrapper<FtUser>().eq("open_id", userId));
    }

    @Override
    public FtUser checkPhone(String phone, String code) {
        //通过手机号查询用户
        FtUser user = ftUserMapper.selectOne(new QueryWrapper<FtUser>()
                .eq("phone", phone));

        if (user == null) {
            user = new FtUser();
            user.setUserType("0");
            user.setPhone(phone);
            return user;
        }

        log.warn("check phone[{}],code[{}],userType[{}]", phone, code, user.getUserType());
        if (Objects.nonNull(user.getUserType()) && StringUtils.isNotEmpty(phone)) {
            String[] codeArray = WechatUtil.getOpenId(code);
            String openId = codeArray[0];
            this.ftUserMapper.updateOpenIdByPhone(openId, phone);
        }
        return user;
    }

    @Override
    public Boolean updateUserInfo(WechatUserInfo userInfo) {
        FtUser ftUser = ftUserMapper.selectOne(new QueryWrapper<FtUser>()
                .eq("open_id", userInfo.getOpenId()));

        ftUser.setAvatar(userInfo.getAvatar());
        ftUser.setName(userInfo.getName());
        ftUser.setAddress(userInfo.getAddress());
        ftUser.setHomeId(userInfo.getHomeId());
        ftUser.setDormType(userInfo.getDormType());

        ftUser.setSex(userInfo.getSex());
        int i = ftUserMapper.updateById(ftUser);
        return i == 1;
    }

    @Override
    public String change(UserRequest request) {
        String phone = request.getPhone();
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
        ftUserMapper.updatePasswordByPhone(newPassword, phone);
        return "修改成功";
    }

    @Override
    public String updateAddress(UserRequest request) {
        String phone = request.getPhone();
        String address = request.getAddress();

        if (StringUtils.isEmpty(phone)) {
            throw new ServiceException("手机号不能为空");
        }

        if (StringUtils.isEmpty(address)) {
            throw new ServiceException("地址不能为空");
        }

        FtUser user = ftUserMapper.selectOne(new QueryWrapper<FtUser>().eq("phone", phone));

        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        ftUserMapper.updateAddressByPhone(address, phone);
        return "修改地址成功";
    }

    private FtUser checkLoginUser(String phone, String password) {
        FtUser user = ftUserMapper.selectOne(new QueryWrapper<FtUser>().eq("phone", phone));

        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        if (!user.getPassword().equals(password)) {
            throw new ServiceException("密码错误");
        }
        return user;
    }

    @Override
    protected void customSelectPage(IPage<UserResponse> page, UserRequest request) {
        ftUserMapper.selectPage(page, request);
    }
}
