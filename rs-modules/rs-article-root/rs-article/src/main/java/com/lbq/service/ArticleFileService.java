package com.lbq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbq.pojo.ArticleFile;

import java.util.Collection;
import java.util.List;

/**
 * 文章文件
 *
 * @author lbq
 * @since 2024-04-14
 */
public interface ArticleFileService extends IService<ArticleFile> {

    List<ArticleFile> listByArticleId(Integer articleId);

    List<String> getArticleFiles();

    void saveByArticle(Integer articleId, List<ArticleFile> articleFiles);

    void removeByArticleIds(Collection<Integer> articleIds);
}
