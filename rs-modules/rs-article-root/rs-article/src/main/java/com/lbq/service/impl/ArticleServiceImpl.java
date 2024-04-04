package com.lbq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.constants.ArticleConstants;
import com.lbq.constants.StatusConstants;
import com.lbq.context.BaseContext;
import com.lbq.mapper.ArticleMapper;
import com.lbq.openfeign.SystemOpenfeign;
import com.lbq.pojo.Article;
import com.lbq.pojo.ArticleTag;
import com.lbq.pojo.ArticleUpvote;
import com.lbq.pojo.Comment;
import com.lbq.service.*;
import com.lbq.utils.IdsReq;
import com.lbq.utils.TreeUtils;
import com.lbq.vo.ArticleVo;
import com.lbq.vo.CommentVo;
import com.lbq.vo.PageVo;
import com.lbq.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    private CommentService commentService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SystemOpenfeign systemOpenfeign;

    @Autowired
    private ArticleUpvoteService articleUpvoteService;

    @Override
    public Page<ArticleVo> page(PageVo pageVo, String keyword) {
        Page<ArticleVo> result = new Page<>(pageVo.getPageNo(), pageVo.getPageSize());
        Page<Article> page = new Page<>(pageVo.getPageNo(), pageVo.getPageSize());
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        Page<Article> resPage = super.page(page, queryWrapper);
        List<ArticleVo> articleVos = this.setView(resPage.getRecords(), false);
        result.setRecords(articleVos);
        result.setTotal(resPage.getTotal());
        return result;
    }

    @Override
    public ArticleVo getById(Integer id) {
        Article article = super.getById(id);
        if (article != null) {
            List<ArticleVo> articleVos = this.setView(Arrays.asList(article), true);
            return articleVos.get(0);
        }
        return null;
    }

    @Override
    @Transactional
    public void add(ArticleVo articleVo) {
        Article article = articleVo.getArticle();
        article.setCreateBy(BaseContext.getUsername());
        article.setCreateTime(new Date());
        boolean save = super.save(article);
        if (!save) {
            throw new RuntimeException("文章发布失败!");
        }
        List<TagVo> tagVos = articleVo.getTagVos();
        List<Integer> tagIds = tagVos.stream().map(TagVo::getId).collect(Collectors.toList());
        articleTagService.saveByArticle(article.getId(), tagIds);
    }

    @Override
    @Transactional
    public void edit(ArticleVo articleVo) {
        Article article = articleVo.getArticle();
        List<TagVo> tagVos = articleVo.getTagVos();
        List<Integer> tagIds = tagVos.stream().map(TagVo::getId).collect(Collectors.toList());
        Article ori = super.getById(article.getId());
        if (ori == null) {
            throw new RuntimeException("文章不存在!");
        }
        ori.setTitle(article.getTitle());
        ori.setContent(article.getContent());
        // 先删除原来的标签，再新增新的标签
        articleTagService.removeByArticleId(article.getId());
        articleTagService.saveByArticle(article.getId(), tagIds);
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

    @Override
    public void comment(Comment comment) {
        comment.setCreateBy(BaseContext.getUsername());
        comment.setCreateTime(new Date());
        commentService.save(comment);
    }

    @Override
    public void deleteComment(Collection<Integer> commentIds) {
        commentService.removeByIds(commentIds);
    }

    @Override
    public List<ArticleVo> setView(List<Article> articles, boolean isDetail) {
        if (CollectionUtils.isEmpty(articles)) {
            return Collections.EMPTY_LIST;
        }
        Map<Integer, TagVo> tagVoMap = systemOpenfeign.getMapByIds(new IdsReq());
        List<ArticleVo> articleVos = new ArrayList<>();
        for (Article record : articles) {
            Integer id = record.getId();
            ArticleVo articleVo = new ArticleVo();
            articleVo.setArticle(record);
            List<ArticleTag> articleTags = articleTagService.listByArticleId(id);
            List<TagVo> tagVos = new ArrayList<>();
            for (ArticleTag articleTag : articleTags) {
                tagVos.add(tagVoMap.get(articleTag.getTagId()));
            }
            articleVo.setTagVos(tagVos);
            if (isDetail) {
                List<ArticleUpvote> articleUpvotes = articleUpvoteService.listByArticleId(id);
                articleVo.setArticleUpvotes(articleUpvotes);
                List<Comment> comments = commentService.listByArticleId(id);
                List<CommentVo> commentVos = TreeUtils.convertToTree(comments);
                articleVo.setCommentVos(commentVos);
            }
            articleVos.add(articleVo);
        }
        return articleVos;
    }
}
