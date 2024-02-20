package com.lbq.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lbq.pojo.ScheduleJob;
import com.lbq.service.ScheduleJobService;
import com.lbq.vo.PageVo;
import com.lbq.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/run")
    public R<?> run(@RequestBody ScheduleJob scheduleJob) {
        scheduleJobService.run(scheduleJob);
        return R.success("执行成功!");
    }

}

