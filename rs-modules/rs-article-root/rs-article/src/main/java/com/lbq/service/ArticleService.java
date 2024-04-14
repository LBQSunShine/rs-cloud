package com.lbq.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lbq.pojo.Article;
import com.lbq.pojo.Comment;
import com.lbq.vo.ArticleVo;
import com.lbq.vo.PageVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

/**
 * 文章
 *
 * @author lbq
 * @since 2024-03-10
 */
public interface ArticleService extends IService<Article> {

    Page<ArticleVo> page(PageVo pageVo, String keyword);

    ArticleVo getById(Integer id);

    void add(ArticleVo articleVo);

    void edit(ArticleVo articleVo);

    void upvote(Article article);

    void unUpvote(Article article);

    void comment(Comment comment);

    void deleteComment(Collection<Integer> commentIds);

    List<ArticleVo> setView(List<Article> articles, boolean isDetail);

    String upload(MultipartFile file);
}
