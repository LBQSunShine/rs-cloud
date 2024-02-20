package com.lbq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.mapper.JobLogMapper;
import com.lbq.pojo.JobLog;
import com.lbq.service.JobLogService;
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
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper, JobLog> implements JobLogService {


}
