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

    private String avatar;

    private String sex;

    private String email;

    private String phone;

    private String token;

    private String signature;

    private String background;

    private String refreshToken;
}
