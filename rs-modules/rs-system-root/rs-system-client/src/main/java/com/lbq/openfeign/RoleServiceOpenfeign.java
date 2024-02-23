package com.lbq.openfeign;

import com.lbq.config.OpenfeignConfig;
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
@FeignClient(value = "system", configuration = OpenfeignConfig.class)
public interface RoleServiceOpenfeign {

    @PostMapping("/sys/openfeign/addUserRole")
    void addUserRole(@RequestParam(name = "userId") Integer userId, @RequestParam(name = "roleCode") String roleCode);
}
