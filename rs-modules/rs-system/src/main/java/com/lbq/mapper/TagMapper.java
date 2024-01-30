package com.lbq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lbq.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;

/**
 * 标签
 *
 * @author lbq
 * @since 2024-01-30
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    int add(Tag tag);
}
