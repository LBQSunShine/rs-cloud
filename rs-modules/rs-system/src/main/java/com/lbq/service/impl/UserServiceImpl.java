package com.lbq.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.constants.StatusConstants;
import com.lbq.mapper.UserMapper;
import com.lbq.pojo.User;
import com.lbq.service.UserService;
import com.lbq.vo.PageVo;
import com.lbq.vo.SortField;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户
 *
 * @author lbq
 * @since 2024-01-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Page<User> page(PageVo pageVo, String keyword) {
        Page<User> page = new Page<>(pageVo.getPageNo(), pageVo.getPageSize());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.lambda().and(wrapper -> {
                wrapper.like(User::getUsername, keyword)
                        .or()
                        .like(User::getNickname, keyword);
            });
        }
        String sortField = pageVo.getSortField();
        if (StringUtils.isNotBlank(sortField)) {
            String sortType = pageVo.getSortType();
            if (SortField.ASC.equals(sortType)) {
                queryWrapper.orderByAsc(sortField);
            } else {
                queryWrapper.orderByDesc(sortField);
            }
        }
        Page<User> res = super.page(page, queryWrapper);
        return res;
    }

    @Override
    @Transactional
    public void edit(User user) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(User::getId, user.getId())
                .set(User::getNickname, user.getNickname())
                .set(User::getEmail, user.getEmail())
                .set(User::getPhone, user.getPhone())
                .set(User::getAvatar, user.getAvatar())
                .set(User::getSex, user.getSex());
        boolean update = super.update(updateWrapper);
        if (!update) {
            throw new RuntimeException("修改失败!");
        }
    }

    @Override
    @Transactional
    public void enable(User user) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(User::getId, user.getId())
                .eq(User::getStatus, StatusConstants.DISABLE)
                .set(User::getStatus, StatusConstants.ENABLE);
        boolean update = super.update(updateWrapper);
        if (!update) {
            throw new RuntimeException("状态变更，请刷新重试!");
        }
    }

    @Override
    @Transactional
    public void disable(User user) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(User::getId, user.getId())
                .eq(User::getStatus, StatusConstants.DISABLE)
                .set(User::getStatus, StatusConstants.ENABLE);
        boolean update = super.update(updateWrapper);
        if (!update) {
            throw new RuntimeException("状态变更，请刷新重试!");
        }
    }
}
