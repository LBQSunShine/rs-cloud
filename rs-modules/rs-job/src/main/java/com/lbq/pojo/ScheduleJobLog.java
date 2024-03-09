package com.lbq.pojo;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lbq.context.BaseContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 调度日志
 *
 * @author lbq
 * @since 2024-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("rs_schedule_job_log")
public class ScheduleJobLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String jobName;

    private String jobGroup;

    private String invokeTarget;

    private String message;

    private String status;

    private String startBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startIme;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public ScheduleJobLog(ScheduleJob scheduleJob) {
        this.jobName = scheduleJob.getJobName();
        this.jobGroup = scheduleJob.getJobGroup();
        this.invokeTarget = scheduleJob.getInvokeTarget();
        this.startBy = StringUtils.isNotBlank(BaseContext.getUsername()) ? BaseContext.getUsername() : "Timer";
        this.startIme = new Date();
    }
}
