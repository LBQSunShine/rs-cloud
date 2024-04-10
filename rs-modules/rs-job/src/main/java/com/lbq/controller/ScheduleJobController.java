package com.lbq.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lbq.function.ActionFunction;
import com.lbq.pojo.ScheduleJob;
import com.lbq.service.ScheduleJobService;
import com.lbq.utils.IdsReq;
import com.lbq.utils.StringFormatUtils;
import com.lbq.vo.PageVo;
import com.lbq.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 调度
 *
 * @author lbq
 * @since 2024-02-20
 */
@RestController
@RequestMapping("/job")
public class ScheduleJobController {

    @Autowired
    private ScheduleJobService scheduleJobService;

    @GetMapping("/page")
    public R<?> page(PageVo pageVo, @RequestParam(name = "keyword", required = false) String keyword) {
        Page<ScheduleJob> res = scheduleJobService.page(pageVo, keyword);
        return R.success(res);
    }

    @PostMapping("/add")
    public R<?> add(@RequestBody ScheduleJob scheduleJob) {
        scheduleJobService.add(scheduleJob);
        return R.success("新增成功!");
    }

    @PostMapping("/edit")
    public R<?> edit(@RequestBody ScheduleJob scheduleJob) {
        scheduleJobService.edit(scheduleJob);
        return R.success("编辑成功!");
    }

    @PostMapping("/run")
    public R<?> run(@RequestBody ScheduleJob scheduleJob) {
        scheduleJobService.run(scheduleJob);
        return R.success("执行成功!");
    }


    @PostMapping("/enable")
    public R<?> enable(@RequestBody IdsReq idsReq) {
        List<String> message = this.action(idsReq.getIds(), "启用", (scheduleJob) -> {
            scheduleJobService.enable(scheduleJob);
        });
        return R.success(message);
    }

    @PostMapping("/disable")
    public R<?> disable(@RequestBody IdsReq idsReq) {
        List<String> message = this.action(idsReq.getIds(), "禁用", (scheduleJob) -> {
            scheduleJobService.disable(scheduleJob);
        });
        return R.success(message);
    }

    private List<String> action(Collection<Integer> ids, String option, ActionFunction<ScheduleJob> fun) {
        List<ScheduleJob> tags = scheduleJobService.listByIds(ids);
        List<String> message = new ArrayList<>();
        for (ScheduleJob scheduleJob : tags) {
            String jobName = scheduleJob.getJobName();
            try {
                fun.callback(scheduleJob);
                String format = StringFormatUtils.format("定时任务{}{}成功！", jobName, option);
                message.add(format);
            } catch (Exception e) {
                String format = StringFormatUtils.format("定时任务{}{}失败！失败原因：{}", jobName, option, e.getMessage());
                message.add(format);
            }
        }
        return message;
    }

}

