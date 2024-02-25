package com.lbq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.mapper.OperLogMapper;
import com.lbq.pojo.OperLog;
import com.lbq.service.OperLogService;
import org.springframework.stereotype.Service;

/**
 * 日志
 *
 * @author lbq
 * @since 2024-02-25
 */
@Service
public class OperLogServiceImpl extends ServiceImpl<OperLogMapper, OperLog> implements OperLogService {

}
