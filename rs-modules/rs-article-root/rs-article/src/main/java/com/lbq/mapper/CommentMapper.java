package com.lbq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lbq.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评论
 *
 * @author lbq
 * @since 2024-03-31
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
