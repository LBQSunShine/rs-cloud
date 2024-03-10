package com.lbq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.mapper.ArticleTagMapper;
import com.lbq.pojo.ArticleTag;
import com.lbq.service.ArticleTagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章标签
 *
 * @author lbq
 * @since 2024-03-10
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

    @Override
    public void saveByArticle(Integer articleId, List<Integer> tagIds) {
        List<ArticleTag> articleTags = new ArrayList<>();
        for (Integer tagId : tagIds) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setTagId(tagId);
            articleTag.setArticleId(articleId);
            articleTags.add(articleTag);
        }
        if (CollectionUtils.isNotEmpty(articleTags)) {
            super.saveBatch(articleTags);
        }
    }

    @Override
    public void removeByArticleId(Integer articleId) {
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, articleId);
        super.remove(queryWrapper);
    }
}
