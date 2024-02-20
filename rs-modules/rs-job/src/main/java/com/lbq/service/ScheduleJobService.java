package com.lbq.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lbq.pojo.ScheduleJob;
import com.lbq.vo.PageVo;

/**
 * 调度
 *
 * @Author: lbq
 * @Date: 2024-02-20
 * @Version: 1.0
 */
public interface ScheduleJobService {
    Page<ScheduleJob> page(PageVo pageVo, String keyword);

    void add(ScheduleJob scheduleJob);

    void run(ScheduleJob scheduleJob);
}
