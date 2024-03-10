package com.lbq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbq.pojo.ArticleUpvote;

/**
 * 文章点赞
 *
 * @author lbq
 * @since 2024-03-10
 */
public interface ArticleUpvoteService extends IService<ArticleUpvote> {

    void saveArticleUpvote();
}
