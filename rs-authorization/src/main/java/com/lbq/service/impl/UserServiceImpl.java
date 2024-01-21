package com.lbq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.constants.RoleConstants;
import com.lbq.constants.TokenConstants;
import com.lbq.mapper.UserMapper;
import com.lbq.openfeign.RoleServiceOpenfeign;
import com.lbq.pojo.User;
import com.lbq.service.RedisService;
import com.lbq.service.RedissonService;
import com.lbq.service.UserService;
import com.lbq.utils.JwtUtils;
import com.lbq.utils.SecurityUtils;
import com.lbq.utils.UUIDUtils;
import com.lbq.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户
 *
 * @Author: lbq
 * @Date: 2024/1/12
 * @Version: 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private RedissonService redissonService;
    @Autowired
    private RoleServiceOpenfeign roleServiceOpenfeign;

    @Override
    public LoginUser login(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new RuntimeException("请输入账号或密码!");
        }
        User user = this.getByUsername(username);
        if (user == null) {
            throw new RuntimeException("账号或密码错误!");
        }
        boolean verify = SecurityUtils.verify(password, user.getPassword());
        if (!verify) {
            throw new RuntimeException("账号或密码错误!");
        }
        Integer id = user.getId();
        String nickname = user.getNickname() == null ? "" : user.getNickname();
        String uuid = UUIDUtils.getUUID();
        Map<String, Object> claims = new HashMap<>();
        claims.put(TokenConstants.USER_ID, id);
        claims.put(TokenConstants.USER_KEY, uuid);
        claims.put(TokenConstants.USER_USER_NAME, username);
        claims.put(TokenConstants.USER_NICK_NAME, nickname);
        String token = JwtUtils.sign(claims);
        Long timeout = 10 * 60L;
        redisService.set(uuid, token, timeout);
        LoginUser loginUser = new LoginUser();
        loginUser.setId(id);
        loginUser.setUsername(username);
        loginUser.setNickname(nickname);
        loginUser.setToken(token);
        return loginUser;
    }

    @Override
    public void register(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new RuntimeException("请输入账号或密码!");
        }
        int usernameLength = username.length();
        if (usernameLength < 2 || usernameLength > 15) {
            throw new RuntimeException("账号长度必须在2到15个字符之间!");
        }
        int passwordLength = password.length();
        if (passwordLength < 5 || passwordLength > 20) {
            throw new RuntimeException("密码长度必须在5到20个字符之间!");
        }
        String encrypt = SecurityUtils.encrypt(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encrypt);
        boolean save = super.save(user);
        if (save) {
            roleServiceOpenfeign.addUserRole(user.getId(), RoleConstants.NORMAL);
        }
    }

    @Override
    public void logout(String token) {
        String key = JwtUtils.getKey(token);
        redisService.delete(key);
    }

    @Override
    @Transactional
    public void editPassword(String username, String password, String newPassword) {
        if (StringUtils.isBlank(password)) {
            throw new RuntimeException("请输入当前密码!");
        }
        if (StringUtils.isBlank(newPassword)) {
            throw new RuntimeException("请输入新密码!");
        }
        int length = newPassword.length();
        if (length < 5 || length > 20) {
            throw new RuntimeException("密码长度必须在5到20个字符之间!");
        }
        if (password.equals(newPassword)) {
            throw new RuntimeException("新密码与当前密码相同!");
        }
        redissonService.tryLockExecute(username, () -> {
            User user = this.getByUsername(username);
            boolean verify = SecurityUtils.verify(password, user.getPassword());
            if (!verify) {
                throw new RuntimeException("当前密码错误!");
            }
            String encrypt = SecurityUtils.encrypt(newPassword);
            LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper
                    .eq(User::getUsername, username)
                    .set(User::getPassword, encrypt);
            super.update(updateWrapper);
            return true;
        });
    }

    private User getByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(User::getUsername, username);
        return super.getOne(queryWrapper);
    }
}
