package com.lbq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.mapper.CommentMapper;
import com.lbq.pojo.Comment;
import com.lbq.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 评论
 *
 * @author lbq
 * @since 2024-03-31
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    public List<Comment> listByArticleId(Integer articleId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Comment::getArticleId, articleId)
                .orderByDesc(Comment::getCreateTime);
        return super.list(queryWrapper);
    }

    @Override
    public void removeByArticleIds(Collection<Integer> articleIds) {
        if (CollectionUtils.isEmpty(articleIds)) {
            return;
        }
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Comment::getArticleId, articleIds);
        super.remove(queryWrapper);
    }
}
