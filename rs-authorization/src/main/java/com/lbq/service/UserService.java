package com.lbq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbq.pojo.User;
import com.lbq.vo.LoginUser;

/**
 * 用户
 *
 * @Author: lbq
 * @Date: 2024/1/12
 * @Version: 1.0
 */
public interface UserService extends IService<User> {

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    LoginUser login(String username, String password);

    /**
     * 注册
     *
     * @param username
     * @param password
     */
    void register(String username, String password);

    /**
     * 退出登录
     *
     * @param token
     */
    void logout(String token);
}
