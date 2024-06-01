package com.lbq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbq.pojo.UserRole;

import java.util.Collection;
import java.util.List;

/**
 * 用户角色关系
 *
 * @author lbq
 * @since 2024-01-21
 */
public interface UserRoleService extends IService<UserRole> {

    List<UserRole> listByUserIds(Collection<Integer> userIds);

    void addUserRole(Integer userId, String roleCode);

    boolean hasAuth(String username);
}
