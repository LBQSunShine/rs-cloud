package com.lbq.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lbq.vo.OperLogVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 日志
 *
 * @author lbq
 * @since 2024-02-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("rs_oper_log")
public class OperLog implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String title;

    private String method;

    private String requestMethod;

    private String operBy;

    private String operUrl;

    private String operParam;

    private String operResult;

    private String errorMsg;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    private Long costTime;

    private String status;

    public OperLog(OperLogVo operLogVo) {
        this.title = operLogVo.getTitle();
        this.method = operLogVo.getMethod();
        this.requestMethod = operLogVo.getRequestMethod();
        this.operBy = operLogVo.getOperBy();
        this.operUrl = operLogVo.getOperUrl();
        this.operParam = operLogVo.getOperParam();
        this.operResult = operLogVo.getOperResult();
        this.errorMsg = operLogVo.getErrorMsg();
        this.operTime = operLogVo.getOperTime();
        this.costTime = operLogVo.getCostTime();
        this.status = operLogVo.getStatus();
    }
}
