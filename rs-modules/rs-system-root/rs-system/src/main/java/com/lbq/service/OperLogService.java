package com.lbq.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lbq.pojo.OperLog;
import com.lbq.vo.PageVo;

/**
 * 日志
 *
 * @author lbq
 * @since 2024-02-25
 */
public interface OperLogService extends IService<OperLog> {

    Page<OperLog> page(PageVo pageVo, String keyword);

}
