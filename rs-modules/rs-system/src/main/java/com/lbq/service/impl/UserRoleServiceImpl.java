package com.lbq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.mapper.UserRoleMapper;
import com.lbq.pojo.UserRole;
import com.lbq.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 用户角色关系
 *
 * @author lbq
 * @since 2024-01-21
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    public List<UserRole> listByUserIds(Collection<Integer> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.EMPTY_LIST;
        }
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserRole::getUserId, userIds);
        return super.list(queryWrapper);
    }
}
