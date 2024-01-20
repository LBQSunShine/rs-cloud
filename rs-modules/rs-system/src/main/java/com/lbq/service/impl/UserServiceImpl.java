package com.lbq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.mapper.UserMapper;
import com.lbq.pojo.User;
import com.lbq.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户
 *
 * @author lbq
 * @since 2024-01-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
