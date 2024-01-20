package com.lbq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.mapper.UserRoleMapper;
import com.lbq.pojo.UserRole;
import com.lbq.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户角色关系
 *
 * @author lbq
 * @since 2024-01-21
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
