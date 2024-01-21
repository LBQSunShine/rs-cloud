package com.lbq.vo;

import lombok.Data;

/**
 * 修改密码传入参数
 *
 * @Author lbq
 * @Date 2024/1/21
 * @Version: 1.0
 */
@Data
public class PasswordVo extends LoginVo {
    private String newPassword;
}
