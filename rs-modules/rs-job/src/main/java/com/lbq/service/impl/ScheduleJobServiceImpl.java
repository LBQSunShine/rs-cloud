package com.lbq.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.constants.StatusConstants;
import com.lbq.context.BaseContext;
import com.lbq.mapper.ScheduleJobMapper;
import com.lbq.pojo.ScheduleJob;
import com.lbq.pojo.ScheduleJobLog;
import com.lbq.service.RedissonService;
import com.lbq.service.ScheduleJobLogService;
import com.lbq.service.ScheduleJobService;
import com.lbq.utils.CronUtils;
import com.lbq.utils.ScheduleUtils;
import com.lbq.vo.PageVo;
import com.lbq.vo.SortField;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * 调度
 *
 * @author lbq
 * @since 2024-02-20
 */
@Service
@Transactional
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobMapper, ScheduleJob> implements ScheduleJobService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ScheduleJobLogService scheduleJobLogService;

    @Autowired
    private RedissonService redissonService;

    /**
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
     */
    @PostConstruct
    public void init() throws SchedulerException {
        scheduler.clear();
        List<ScheduleJob> jobList = super.list();
        for (ScheduleJob scheduleJob : jobList) {
            ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
        }
    }

    @Override
    public Page<ScheduleJob> page(PageVo pageVo, String keyword) {
        Page<ScheduleJob> page = new Page<>(pageVo.getPageNo(), pageVo.getPageSize());
        QueryWrapper<ScheduleJob> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.lambda().and(wrapper -> {
                wrapper.like(ScheduleJob::getJobName, keyword);
            });
        }
        String sortField = pageVo.getSortField();
        if (StringUtils.isNotBlank(sortField)) {
            String sortType = pageVo.getSortType();
            if (SortField.ASC.equals(sortType)) {
                queryWrapper.orderByAsc(sortField);
            } else {
                queryWrapper.orderByDesc(sortField);
            }
        }
        Page<ScheduleJob> res = super.page(page, queryWrapper);
        return res;
    }

    @Override
    public void add(ScheduleJob scheduleJob) {
        if (!CronUtils.isValid(scheduleJob.getCronExpression())) {
            throw new RuntimeException("Cron表达式不正确!");
        }
        scheduleJob.setStatus(StatusConstants.ENABLE);
        scheduleJob.setCreateBy(BaseContext.getUsername());
        scheduleJob.setCreateTime(new Date());
        boolean save = super.save(scheduleJob);
        if (!save) {
            throw new RuntimeException("调度任务创建失败!");
        }
        ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
    }

    @Override
    public void run(ScheduleJob scheduleJob) {
        ScheduleJob job = super.getById(scheduleJob.getId());
        if (job == null) {
            throw new RuntimeException("调度任务不存在!");
        }
        ScheduleJobLog scheduleJobLog = new ScheduleJobLog(job);
        try {
            JobKey jobKey = ScheduleUtils.getJobKey(job.getId(), job.getJobGroup());
            if (scheduler.checkExists(jobKey)) {
                scheduler.triggerJob(jobKey);
            }
            scheduleJobLog.setStatus(StatusConstants.SUCCESS);
        } catch (SchedulerException e) {
            String message = "调度任务不存在!";
            scheduleJobLog.setStatus(StatusConstants.FAIL);
            scheduleJobLog.setMessage(message);
            throw new RuntimeException(message);
        } finally {
            scheduleJobLog.setEndTime(new Date());
            scheduleJobLogService.save(scheduleJobLog);
        }
    }

    @Override
    public void edit(ScheduleJob scheduleJob) {
        if (!CronUtils.isValid(scheduleJob.getCronExpression())) {
            throw new RuntimeException("Cron表达式不正确!");
        }
        Integer id = scheduleJob.getId();
        Boolean res = redissonService.tryLockExecute(String.valueOf(id), () -> {
            LambdaQueryWrapper<ScheduleJob> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .eq(ScheduleJob::getId, scheduleJob.getId())
                    .eq(ScheduleJob::getStatus, StatusConstants.DISABLE);
            boolean remove = super.remove(queryWrapper);
            if (!remove) {
                throw new RuntimeException("状态变更，请刷新重试!");
            }
            scheduleJob.setCreateBy(BaseContext.getUsername());
            scheduleJob.setCreateTime(new Date());
            super.save(scheduleJob);
            ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
            return true;
        });
        if (res == null) {
            throw new RuntimeException("状态变更，请刷新重试!");
        }
    }

    @Override
    public void enable(ScheduleJob scheduleJob) {
        Integer id = scheduleJob.getId();
        Boolean res = redissonService.tryLockExecute(String.valueOf(id), () -> {
            LambdaUpdateWrapper<ScheduleJob> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper
                    .eq(ScheduleJob::getId, scheduleJob.getId())
                    .eq(ScheduleJob::getStatus, StatusConstants.DISABLE)
                    .set(ScheduleJob::getStatus, StatusConstants.ENABLE)
                    .set(ScheduleJob::getUpdateBy, BaseContext.getUsername())
                    .set(ScheduleJob::getUpdateTime, new Date());
            boolean update = super.update(updateWrapper);
            if (!update) {
                throw new RuntimeException("状态变更，请刷新重试!");
            }
            ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
            return true;
        });
        if (res == null) {
            throw new RuntimeException("状态变更，请刷新重试!");
        }
    }

    @Override
    public void disable(ScheduleJob scheduleJob) {
        Integer id = scheduleJob.getId();
        Boolean res = redissonService.tryLockExecute(String.valueOf(id), () -> {
            LambdaUpdateWrapper<ScheduleJob> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper
                    .eq(ScheduleJob::getId, scheduleJob.getId())
                    .eq(ScheduleJob::getStatus, StatusConstants.ENABLE)
                    .set(ScheduleJob::getStatus, StatusConstants.DISABLE)
                    .set(ScheduleJob::getUpdateBy, BaseContext.getUsername())
                    .set(ScheduleJob::getUpdateTime, new Date());
            boolean update = super.update(updateWrapper);
            if (!update) {
                throw new RuntimeException("状态变更，请刷新重试!");
            }
            ScheduleUtils.deleteScheduleJob(scheduler, scheduleJob);
            return true;
        });
        if (res == null) {
            throw new RuntimeException("状态变更，请刷新重试!");
        }
    }
}
