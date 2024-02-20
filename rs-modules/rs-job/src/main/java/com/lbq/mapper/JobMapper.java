package com.lbq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lbq.pojo.Job;
import org.apache.ibatis.annotations.Mapper;

/**
 * 调度
 *
 * @author lbq
 * @since 2024-02-20
 */
@Mapper
public interface JobMapper extends BaseMapper<Job> {

}
