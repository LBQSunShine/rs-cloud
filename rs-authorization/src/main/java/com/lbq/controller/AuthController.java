package com.lbq.controller;


import com.lbq.service.UserService;
import com.lbq.utils.SecurityUtils;
import com.lbq.vo.LoginUser;
import com.lbq.vo.LoginVo;
import com.lbq.vo.R;
import com.lbq.vo.RegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户
 *
 * @Author: lbq
 * @Date: 2024/1/12
 * @Version: 1.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<?> login(@RequestBody LoginVo loginVo) {
        LoginUser loginUser = userService.login(loginVo.getUsername(), loginVo.getPassword());
        return R.success(loginUser);
    }

    @PostMapping("/register")
    public R<?> register(@RequestBody RegisterVo registerVo) {
        try {
            userService.register(registerVo.getUsername(), registerVo.getPassword());
        } catch (Exception e) {
            throw new RuntimeException("账号已注册!");
        }
        return R.success("注册成功!");
    }

    @PostMapping("/logout")
    public R<?> logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        userService.logout(token);
        return R.success("退出登录成功!");
    }
//
//    @PostMapping("/refresh")
//    public R<?> refresh(@RequestBody LoginVo loginVo) {
//
//    }
}

