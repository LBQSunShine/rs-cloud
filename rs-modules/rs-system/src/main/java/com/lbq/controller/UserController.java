package com.lbq.controller;

import com.lbq.pojo.Role;
import com.lbq.pojo.User;
import com.lbq.pojo.UserRole;
import com.lbq.service.RoleService;
import com.lbq.service.UserRoleService;
import com.lbq.service.UserService;
import com.lbq.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}

