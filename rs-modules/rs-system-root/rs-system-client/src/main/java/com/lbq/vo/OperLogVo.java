package com.lbq.vo;

import lombok.Data;

import java.util.Date;

/**
 * 日志
 *
 * @author lbq
 * @since 2024-02-25
 */
@Data
public class OperLogVo {

    private String title;

    private String method;

    private String requestMethod;

    private String operBy;

    private String operUrl;

    private String operParam;

    private String operResult;

    private String errorMsg;

    private Date operTime;

    private Long costTime;

    private String status;
}
