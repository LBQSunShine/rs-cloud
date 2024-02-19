package com.lbq.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * openfeign
 *
 * @Author lbq
 * @Date 2024/1/21
 * @Version: 1.0
 */
@Component
@FeignClient(value = "system")
public interface RoleServiceOpenfeign {

    @PostMapping("/sys/user/addUserRole")
    void addUserRole(@RequestParam(name = "userId") Integer userId, @RequestParam(name = "roleCode") String roleCode);
}
