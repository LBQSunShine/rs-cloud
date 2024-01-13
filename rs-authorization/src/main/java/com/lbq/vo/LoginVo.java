package com.lbq.vo;

import lombok.Data;

/**
 * 登录请求参数
 *
 * @Author lbq
 * @Date 2024/1/12
 * @Version: 1.0
 */
@Data
public class LoginVo {
    private String username;
    private String password;
}
