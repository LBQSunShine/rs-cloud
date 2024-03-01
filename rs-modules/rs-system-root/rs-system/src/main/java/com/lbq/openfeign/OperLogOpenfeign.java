package com.lbq.openfeign;


import com.lbq.pojo.OperLog;
import com.lbq.service.OperLogService;
import com.lbq.vo.OperLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志
 *
 * @author lbq
 * @since 2024-02-25
 */
@RestController
@RequestMapping("/sys/openfeign/log")
public class OperLogOpenfeign {

    @Autowired
    private OperLogService operLogService;

    @PostMapping("/addOperLog")
    public void addOperLog(@RequestBody OperLogVo operLogVo) {
        OperLog operLog = new OperLog(operLogVo);
        operLogService.save(operLog);
    }
}

