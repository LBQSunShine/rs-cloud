package com.lbq.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lbq.pojo.Article;
import com.lbq.pojo.Comment;
import com.lbq.pojo.Message;
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

    Page<ArticleVo> page(PageVo pageVo, String keyword, String selectType, Collection<Integer> tagIds);

    ArticleVo getById(Integer id);

    Integer add(ArticleVo articleVo);

    Integer edit(ArticleVo articleVo);

    void publish(List<Integer> ids);

    void upvote(Article article);

    void unUpvote(Article article);

    void comment(Comment comment);

    void deleteComment(Collection<Integer> commentIds);

    List<ArticleVo> setView(List<Article> articles, boolean isDetail);

    String upload(MultipartFile file);

    void delete(Collection<Integer> ids);

    void readMessage(Message message);

    List<Message> listMessage();
}
