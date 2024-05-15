package com.lbq.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lbq.pojo.User;
import com.lbq.vo.PageVo;
import com.lbq.vo.UserVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户
 *
 * @author lbq
 * @since 2024-01-21
 */
public interface UserService extends IService<User> {

    Page<User> page(PageVo pageVo, String keyword);

    void setView(List<User> users);

    /**
     * 编辑基本信息
     *
     * @param user
     */
    void edit(User user);

    void enable(User user);

    void disable(User user);

    String upload(MultipartFile file);

    List<String> getUserFiles();

    User getByUsername(String username);

    Map<String, UserVo> getMapByUsernames(Collection<String> usernames);
}
