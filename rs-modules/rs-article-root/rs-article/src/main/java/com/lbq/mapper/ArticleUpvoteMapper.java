package com.lbq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lbq.pojo.ArticleUpvote;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章点赞
 *
 * @author lbq
 * @since 2024-03-10
 */
@Mapper
public interface ArticleUpvoteMapper extends BaseMapper<ArticleUpvote> {

}
