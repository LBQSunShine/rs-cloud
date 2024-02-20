package com.lbq.utils;

import com.lbq.pojo.ScheduleJob;
import org.quartz.*;

/**
 * 定时任务工具类
 *
 * @Author: lbq
 * @Date: 2024/2/20
 * @Version: 1.0
 */
public class ScheduleUtils {

    public static void createScheduleJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        Integer id = scheduleJob.getId();
        String jobGroup = scheduleJob.getJobGroup();
        JobKey jobKey = getJobKey(id, jobGroup);
        TriggerKey triggerKey = getTriggerKey(id, jobGroup);
        String invokeTarget = scheduleJob.getInvokeTarget();
        String cronExpression = scheduleJob.getCronExpression();
        try {
            // 启动调度器
            scheduler.start();
            // 构建job信息
            JobDetail jobDetail = JobBuilder
                    .newJob(getClass(invokeTarget).getClass())
                    .withIdentity(jobKey)
                    .build();
            // 表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException("调度任务创建失败!");
        } catch (Exception e) {
            throw new RuntimeException(StringFormatUtils.format("后台找不到该类名：[{}]!", invokeTarget));
        }
    }

    private static Job getClass(String classname) throws Exception {
        Class<?> clazz = Class.forName(classname);
        return (Job) clazz.newInstance();
    }

    public static JobKey getJobKey(Integer id, String jobGroup) {
        String jobId = String.valueOf(id);
        return JobKey.jobKey(jobId, jobGroup);
    }

    public static TriggerKey getTriggerKey(Integer id, String jobGroup) {
        String jobId = String.valueOf(id);
        return TriggerKey.triggerKey(jobId, jobGroup);
    }
}
