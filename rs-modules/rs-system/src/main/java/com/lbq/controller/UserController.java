package com.lbq.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lbq.function.ActionFunction;
import com.lbq.pojo.Role;
import com.lbq.pojo.User;
import com.lbq.pojo.UserRole;
import com.lbq.service.RoleService;
import com.lbq.service.UserRoleService;
import com.lbq.service.UserService;
import com.lbq.utils.IdsReq;
import com.lbq.utils.StringFormatUtils;
import com.lbq.vo.PageVo;
import com.lbq.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户
 *
 * @author lbq
 * @since 2024-01-21
 */
@RestController
@RequestMapping("/sys/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;

    @GetMapping("/page")
    public R<?> page(PageVo pageVo, @RequestParam(name = "keyword", required = false) String keyword) {
        Page<User> res = userService.page(pageVo, keyword);
        return R.success(res);
    }

    @PostMapping("/edit")
    public R<?> edit(@RequestBody User user) {
        userService.edit(user);
        return R.success("修改成功!");
    }

    @PostMapping("/addUserRole")
    public void addUserRole(@RequestParam(name = "userId") Integer userId, @RequestParam(name = "roleCode") String roleCode) {
        Role role = roleService.getByCode(roleCode);
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(role.getId());
        userRoleService.save(userRole);
    }

    @PostMapping("/enable")
    public R<?> enable(@RequestBody IdsReq idsReq) {
        List<String> message = this.action(idsReq.getIds(), "启用", (user) -> {
            userService.enable(user);
        });
        return R.success(message);
    }

    @PostMapping("/disable")
    public R<?> disable(@RequestBody IdsReq idsReq) {
        List<String> message = this.action(idsReq.getIds(), "禁用", (user) -> {
            userService.disable(user);
        });
        return R.success(message);
    }

    private List<String> action(Collection<Integer> ids, String option, ActionFunction<User> fun) {
        List<User> users = userService.listByIds(ids);
        List<String> message = new ArrayList<>();
        for (User user : users) {
            String username = user.getUsername();
            try {
                fun.callback(user);
                String format = StringFormatUtils.format("账号{}{}成功！", username, option);
                message.add(format);
            } catch (Exception e) {
                String format = StringFormatUtils.format("账号{}{}失败！失败原因：{}", username, option, e.getMessage());
                message.add(format);
            }
        }
        return message;
    }

}

