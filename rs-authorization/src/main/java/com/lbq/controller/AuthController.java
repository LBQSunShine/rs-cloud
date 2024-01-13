package com.lbq.controller;


import com.lbq.service.UserService;
import com.lbq.vo.LoginUser;
import com.lbq.vo.LoginVo;
import com.lbq.vo.R;
import com.lbq.vo.RegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        userService.register(registerVo.getUsername(), registerVo.getPassword());
        return R.success("注册成功!");
    }

    @GetMapping("/get")
    public R<?> get() {
        return R.success("注册成功!");
    }

//    @PostMapping("/logout")
//    public R<?> logout(HttpServletRequest request) {
//
//    }
//
//    @PostMapping("/register")
//    public R<?> register(@RequestBody LoginVo loginVo) {
//
//    }
//
//    @PostMapping("/refresh")
//    public R<?> refresh(@RequestBody LoginVo loginVo) {
//
//    }
}

