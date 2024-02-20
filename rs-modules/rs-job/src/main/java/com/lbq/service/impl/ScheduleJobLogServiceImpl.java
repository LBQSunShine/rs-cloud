package com.lbq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.mapper.ScheduleJobLogMapper;
import com.lbq.pojo.ScheduleJobLog;
import com.lbq.service.ScheduleJobLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 调度日志
 *
 * @author lbq
 * @since 2024-02-20
 */
@Service
@Transactional
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogMapper, ScheduleJobLog> implements ScheduleJobLogService {


}
