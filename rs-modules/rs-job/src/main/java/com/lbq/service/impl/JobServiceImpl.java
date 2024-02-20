package com.lbq.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.mapper.JobMapper;
import com.lbq.pojo.Job;
import com.lbq.service.JobService;
import com.lbq.vo.PageVo;
import com.lbq.vo.SortField;
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

    @Override
    public Page<Job> page(PageVo pageVo, String keyword) {
        Page<Job> page = new Page<>(pageVo.getPageNo(), pageVo.getPageSize());
        QueryWrapper<Job> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.lambda().and(wrapper -> {
                wrapper.like(Job::getJobName, keyword);
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
        Page<Job> res = super.page(page, queryWrapper);
        return res;
    }
}
