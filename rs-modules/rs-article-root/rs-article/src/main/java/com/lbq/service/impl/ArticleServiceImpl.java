package com.lbq.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.constants.ArticleConstants;
import com.lbq.constants.StatusConstants;
import com.lbq.context.BaseContext;
import com.lbq.mapper.ArticleMapper;
import com.lbq.pojo.Article;
import com.lbq.service.ArticleService;
import com.lbq.service.ArticleTagService;
import com.lbq.service.RedisService;
import com.lbq.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文章
 *
 * @author lbq
 * @since 2024-03-10
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private RedisService redisService;

    @Override
    public Page<Article> page(PageVo pageVo, String keyword) {
        return null;
    }

    @Override
    @Transactional
    public void add(Article article) {
        article.setCreateBy(BaseContext.getUsername());
        article.setCreateTime(new Date());
        boolean save = super.save(article);
        if (!save) {
            throw new RuntimeException("文章发布失败!");
        }
        articleTagService.saveByArticle(article.getId(), article.getTagIds());
    }

    @Override
    @Transactional
    public void edit(Article article) {
        Article ori = super.getById(article.getId());
        if (ori == null) {
            throw new RuntimeException("文章不存在!");
        }
        ori.setTitle(article.getTitle());
        ori.setContent(article.getContent());
        // 先删除原来的标签，再新增新的标签
        articleTagService.removeByArticleId(article.getId());
        articleTagService.saveByArticle(article.getId(), article.getTagIds());
    }

    @Override
    public void upvote(Article article) {
        String username = BaseContext.getUsername();
        String key = ArticleConstants.UPVOTE + username;
        Integer id = article.getId();
        String hKey = username + "::" + id;
        if (redisService.hasKey(key)) {
            redisService.hSetValue(key, hKey, StatusConstants.STATUS_1);
        } else {
            Map<String, String> map = new HashMap<>();
            map.put(hKey, StatusConstants.STATUS_1);
            redisService.hSet(key, map);
        }
    }

    @Override
    public void unUpvote(Article article) {
        String username = BaseContext.getUsername();
        String key = ArticleConstants.UPVOTE + username;
        Integer id = article.getId();
        String hKey = username + "::" + id;
        if (redisService.hasKey(key)) {
            redisService.hSetValue(key, hKey, StatusConstants.STATUS_0);
        } else {
            Map<String, String> map = new HashMap<>();
            map.put(hKey, StatusConstants.STATUS_0);
            redisService.hSet(key, map);
        }
    }
}
