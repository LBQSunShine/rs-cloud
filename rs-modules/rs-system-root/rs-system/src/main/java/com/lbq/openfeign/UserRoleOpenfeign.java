package com.lbq.openfeign;

import com.lbq.pojo.Role;
import com.lbq.pojo.UserRole;
import com.lbq.service.RoleService;
import com.lbq.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lbq
 * @Date: 2024/2/20
 * @Version: 1.0
 */
@RestController
@RequestMapping("/sys/openfeign")
public class UserRoleOpenfeign {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;

    @PostMapping("/addUserRole")
    public void addUserRole(@RequestParam(name = "userId") Integer userId, @RequestParam(name = "roleCode") String roleCode) {
        Role role = roleService.getByCode(roleCode);
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(role.getId());
        userRoleService.save(userRole);
    }
}
