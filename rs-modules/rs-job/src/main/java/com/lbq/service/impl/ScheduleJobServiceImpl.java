package com.lbq.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.constants.StatusConstants;
import com.lbq.context.BaseContext;
import com.lbq.mapper.ScheduleJobMapper;
import com.lbq.pojo.ScheduleJob;
import com.lbq.service.ScheduleJobService;
import com.lbq.utils.CronUtils;
import com.lbq.utils.ScheduleUtils;
import com.lbq.vo.PageVo;
import com.lbq.vo.SortField;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
}
