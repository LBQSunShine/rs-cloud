package com.lbq.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.mapper.OperLogMapper;
import com.lbq.pojo.OperLog;
import com.lbq.service.OperLogService;
import com.lbq.vo.PageVo;
import com.lbq.vo.SortField;
import org.springframework.stereotype.Service;

/**
 * 日志
 *
 * @author lbq
 * @since 2024-02-25
 */
@Service
public class OperLogServiceImpl extends ServiceImpl<OperLogMapper, OperLog> implements OperLogService {

    @Override
    public Page<OperLog> page(PageVo pageVo, String keyword) {
        Page<OperLog> page = new Page<>(pageVo.getPageNo(), pageVo.getPageSize());
        QueryWrapper<OperLog> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(keyword)) {

        }
        String sortField = pageVo.getSortField();
        if (StringUtils.isNotBlank(sortField)) {
            String sortType = pageVo.getSortType();
            if (SortField.ASC.equals(sortType)) {
                queryWrapper.orderByAsc(sortField);
            } else {
                queryWrapper.orderByDesc(sortField);
            }
        }
        Page<OperLog> res = super.page(page, queryWrapper);
        return res;
    }
}
