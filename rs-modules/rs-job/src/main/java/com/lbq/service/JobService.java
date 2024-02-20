package com.lbq.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lbq.pojo.Job;
import com.lbq.vo.PageVo;

/**
 * 调度
 *
 * @Author: lbq
 * @Date: 2024-02-20
 * @Version: 1.0
 */
public interface JobService {
    Page<Job> page(PageVo pageVo, String keyword);
}
