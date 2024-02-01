package com.lbq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbq.pojo.Role;

import java.util.Collection;
import java.util.Map;

/**
 * 角色
 *
 * @author lbq
 * @since 2024-01-21
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据编码查询角色
     *
     * @param code
     * @return
     */
    Role getByCode(String code);

    Map<Integer, Role> getMapByIds(Collection<Integer> ids);
}
