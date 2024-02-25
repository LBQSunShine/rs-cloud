package com.lbq.service;

import com.lbq.openfeign.SystemOpenfeign;
import com.lbq.vo.OperLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 日志异步插入
 *
 * @Author lbq
 * @Date 2024/2/25
 * @Version: 1.0
 */
@Service
public class AsyncLogService {

    @Autowired
    private SystemOpenfeign systemOpenfeign;

    /**
     * 保存系统日志记录
     */
    @Async
    public void addOperLog(OperLogVo operLogVo) throws Exception {
        systemOpenfeign.addOperLog(operLogVo);
    }
}
