package com.lbq.vo;

import lombok.Data;

/**
 * 当前登录用户
 *
 * @Author lbq
 * @Date 2024/1/12
 * @Version: 1.0
 */
@Data
public class LoginUser {
    private Integer id;

    private String nickname;

    private String username;

    private String token;

    private String refreshToken;
}
