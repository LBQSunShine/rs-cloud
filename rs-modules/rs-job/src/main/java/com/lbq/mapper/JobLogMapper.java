package com.lbq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lbq.pojo.JobLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 调度日志
 *
 * @author lbq
 * @since 2024-02-20
 */
@Mapper
public interface JobLogMapper extends BaseMapper<JobLog> {

}
