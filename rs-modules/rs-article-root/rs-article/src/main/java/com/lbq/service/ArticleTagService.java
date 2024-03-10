package com.lbq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbq.pojo.ArticleTag;

import java.util.List;

/**
 * 文章标签
 *
 * @author lbq
 * @since 2024-03-10
 */
public interface ArticleTagService extends IService<ArticleTag> {

    void saveByArticle(Integer articleId, List<Integer> tagIds);

    void removeByArticleId(Integer articleId);
}
