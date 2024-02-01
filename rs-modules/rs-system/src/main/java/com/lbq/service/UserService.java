package com.lbq.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lbq.pojo.User;
import com.lbq.vo.PageVo;

/**
 * 用户
 *
 * @author lbq
 * @since 2024-01-21
 */
public interface UserService extends IService<User> {

    Page<User> page(PageVo pageVo, String keyword);

    /**
     * 编辑基本信息
     *
     * @param user
     */
    void edit(User user);

    void enable(User user);

    void disable(User user);
}
