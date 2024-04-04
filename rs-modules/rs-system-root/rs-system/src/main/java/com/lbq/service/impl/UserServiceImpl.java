package com.lbq.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.constants.StatusConstants;
import com.lbq.mapper.UserMapper;
import com.lbq.openfeign.FileServiceOpenfeign;
import com.lbq.pojo.Role;
import com.lbq.pojo.User;
import com.lbq.pojo.UserRole;
import com.lbq.service.RoleService;
import com.lbq.service.UserRoleService;
import com.lbq.service.UserService;
import com.lbq.vo.FileVo;
import com.lbq.vo.PageVo;
import com.lbq.vo.SortField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户
 *
 * @author lbq
 * @since 2024-01-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private FileServiceOpenfeign fileServiceOpenfeign;

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
        this.setView(res.getRecords());
        return res;
    }

    @Override
    public void setView(List<User> users) {
        if (CollectionUtils.isEmpty(users)) {
            return;
        }
        Set<Integer> userIds = users.stream().map(User::getId).collect(Collectors.toSet());
        List<UserRole> userRoles = userRoleService.listByUserIds(userIds);
        Map<Integer, List<UserRole>> userRoleMap = userRoles.stream().collect(Collectors.groupingBy(UserRole::getUserId));
        Set<Integer> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
        Map<Integer, Role> roleMap = roleService.getMapByIds(roleIds);
        for (User record : users) {
            Integer id = record.getId();
            List<UserRole> recordURs = userRoleMap.get(id);
            for (UserRole ur : recordURs) {
                Integer roleId = ur.getRoleId();
                Role role = roleMap.get(roleId);
                if (role != null) {
                    String roles = record.getRoles();
                    if (StringUtils.isBlank(roles)) {
                        roles = role.getName();
                    } else {
                        roles = "," + role.getName();
                    }
                    record.setRoles(roles);
                }
            }
        }
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
                .eq(User::getStatus, StatusConstants.ENABLE)
                .set(User::getStatus, StatusConstants.DISABLE);
        boolean update = super.update(updateWrapper);
        if (!update) {
            throw new RuntimeException("状态变更，请刷新重试!");
        }
    }

    @Override
    public String upload(MultipartFile file) {
        FileVo upload = fileServiceOpenfeign.upload(file);
        return upload.getUrl();
    }
}
