package com.lbq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbq.pojo.Comment;

import java.util.Collection;
import java.util.List;

/**
 * 评论
 *
 * @author lbq
 * @since 2024-03-31
 */
public interface CommentService extends IService<Comment> {

    List<Comment> listByArticleId(Integer articleId);

    void removeByArticleIds(Collection<Integer> articleIds);
}
