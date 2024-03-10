package com.lbq.mapper;

import com.lbq.pojo.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章
 *
 * @author lbq
 * @since 2024-03-10
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

}
