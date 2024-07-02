package com.lbq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbq.pojo.ArticleUpvote;

import java.util.Collection;
import java.util.List;

/**
 * 文章点赞
 *
 * @author lbq
 * @since 2024-03-10
 */
public interface ArticleUpvoteService extends IService<ArticleUpvote> {

    void saveArticleUpvote();

    List<ArticleUpvote> listByArticleId(Integer articleId);

    void removeUnUpvote();

    void removeByArticleIds(Collection<Integer> articleIds);
}
