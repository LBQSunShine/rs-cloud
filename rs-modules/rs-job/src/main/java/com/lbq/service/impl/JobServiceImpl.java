package com.lbq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.mapper.JobMapper;
import com.lbq.pojo.Job;
import com.lbq.service.JobService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 调度
 *
 * @author lbq
 * @since 2024-02-20
 */
@Service
@Transactional
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {


}
