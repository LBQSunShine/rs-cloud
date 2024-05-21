package com.lbq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lbq.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * 文章
 *
 * @author lbq
 * @since 2024-03-10
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    Page<Article> page(Page<Article> page,
                       @Param("keyword") String keyword,
                       @Param("username") String username,
                       @Param("tagIds") Collection<Integer> tagIds);
}
