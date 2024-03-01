package com.lbq.openfeign;

import com.lbq.config.OpenfeignConfig;
import com.lbq.vo.OperLogVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
public interface SystemOpenfeign {

    @PostMapping("/sys/openfeign/user-role/addUserRole")
    void addUserRole(@RequestParam(name = "userId") Integer userId, @RequestParam(name = "roleCode") String roleCode);

    @PostMapping("/sys/openfeign/log/addOperLog")
    void addOperLog(@RequestBody OperLogVo operLogVo);
}
