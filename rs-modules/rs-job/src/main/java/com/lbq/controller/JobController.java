package com.lbq.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lbq.pojo.Job;
import com.lbq.service.JobService;
import com.lbq.vo.PageVo;
import com.lbq.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 调度
 *
 * @author lbq
 * @since 2024-02-20
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/page")
    public R<?> page(PageVo pageVo, @RequestParam(name = "keyword", required = false) String keyword) {
        Page<Job> res = jobService.page(pageVo, keyword);
        return R.success(res);
    }

}

