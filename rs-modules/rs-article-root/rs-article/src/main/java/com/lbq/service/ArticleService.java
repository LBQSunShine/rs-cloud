package com.lbq.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lbq.pojo.Article;
import com.lbq.vo.PageVo;

/**
 * 文章
 *
 * @author lbq
 * @since 2024-03-10
 */
public interface ArticleService extends IService<Article> {

    Page<Article> page(PageVo pageVo, String keyword);

    void add(Article article);

    void edit(Article article);

    void upvote(Article article);

    void unUpvote(Article article);
}
