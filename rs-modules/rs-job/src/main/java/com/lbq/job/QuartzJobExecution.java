package com.lbq.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Author lbq
 * @Date 2024/3/8
 * @Version: 1.0
 */
public class QuartzJobExecution implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 定时处理逻辑

    }
}
