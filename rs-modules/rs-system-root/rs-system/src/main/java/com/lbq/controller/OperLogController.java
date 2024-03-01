package com.lbq.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lbq.pojo.OperLog;
import com.lbq.service.OperLogService;
import com.lbq.vo.PageVo;
import com.lbq.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志
 *
 * @author lbq
 * @since 2024-02-25
 */
@RestController
@RequestMapping("/sys/log")
public class OperLogController {

    @Autowired
    private OperLogService operLogService;

    @GetMapping("/page")
    public R<?> page(PageVo pageVo, @RequestParam(name = "keyword", required = false) String keyword) {
        Page<OperLog> res = operLogService.page(pageVo, keyword);
        return R.success(res);
    }

}

