package com.lbq.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.constants.ArticleConstants;
import com.lbq.constants.StatusConstants;
import com.lbq.context.BaseContext;
import com.lbq.mapper.ArticleMapper;
import com.lbq.openfeign.FileOpenfeign;
import com.lbq.openfeign.SystemOpenfeign;
import com.lbq.pojo.*;
import com.lbq.service.*;
import com.lbq.utils.IdsReq;
import com.lbq.utils.TreeUtils;
import com.lbq.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private ArticleMapper articleMapper;

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

    @Autowired
    private ArticleFileService articleFileService;

    @Autowired
    private FileOpenfeign fileOpenfeign;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private MessageService messageService;

    @Override
    public Page<ArticleVo> page(PageVo pageVo, String keyword, String selectType, Collection<Integer> tagIds) {
        Page<ArticleVo> result = new Page<>(pageVo.getPageNo(), pageVo.getPageSize());
        Page<Article> page = new Page<>(pageVo.getPageNo(), pageVo.getPageSize());
        String username = null;
        if ("MINE".equals(selectType)) {
            username = BaseContext.getUsername();
        }
        Page<Article> resPage = articleMapper.page(page, keyword, username, tagIds);
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
        articleFileService.saveByArticle(article.getId(), articleVo.getArticleFiles());
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
        ori.setType(article.getType());
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
        // 十分钟
        long expireTime = 10 * 60;
        redisService.expire(key, expireTime);
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
        // 十分钟
        long expireTime = 10 * 60;
        redisService.expire(key, expireTime);
    }

    @Override
    public void comment(Comment comment) {
        String username = BaseContext.getUsername();
        Date date = new Date();
        comment.setCreateBy(username);
        comment.setCreateTime(date);
        commentService.save(comment);
        Message message = new Message();
        message.setArticleId(comment.getArticleId());
        message.setType("COMMENT");
        message.setStatus(StatusConstants.STATUS_0);
        message.setCreateBy(username);
        message.setCreateTime(date);
        messageService.save(message);
        webSocketService.sendMessage(username, JSONObject.toJSONString(message));
    }

    @Override
    public void deleteComment(Collection<Integer> commentIds) {
        commentService.removeByIds(commentIds);
    }

    @Override
    public String upload(MultipartFile file) {
        FileVo upload = fileOpenfeign.upload(file);
        return upload.getUrl();
    }

    @Override
    public void delete(Collection<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .in(Article::getId, ids)
                .eq(Article::getCreateBy, BaseContext.getUsername());
        super.remove(queryWrapper);
        // 删除评论
        commentService.removeByArticleIds(ids);
        // 删除点赞
        articleUpvoteService.removeByArticleIds(ids);
        // 删除文件
        articleFileService.removeByArticleIds(ids);
        // 删除标签关联
        articleTagService.removeByArticleIds(ids);
        // 删除所有消息
        messageService.removeByArticleIds(ids);
    }

    @Override
    public void readMessage(Message message) {
        messageService.readMessage(message);
    }

    @Override
    public List<Message> listMessage() {
        return messageService.listMessage();
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
            List<ArticleUpvote> articleUpvotes = articleUpvoteService.listByArticleId(id);
            articleVo.setArticleUpvotes(articleUpvotes);
            articleVo.setUpvoteStatus(StatusConstants.STATUS_0);
            ArticleUpvote articleUpvote = articleUpvotes.stream().filter(item -> item.getUpvoteBy().equals(BaseContext.getUsername()) && StatusConstants.STATUS_1.equals(item.getStatus())).findFirst().orElse(null);
            if (articleUpvote != null) {
                articleVo.setUpvoteStatus(StatusConstants.STATUS_1);
            }
            articleVo.setUpvoteSize(articleUpvotes.size());
            if (isDetail) {
                List<Comment> comments = commentService.listByArticleId(id);
                List<String> usernames = comments.stream().map(Comment::getCreateBy).collect(Collectors.toList());
                Map<String, UserVo> userVoMap = systemOpenfeign.getMapByUsernames(usernames);
                Map<Integer, Comment> commentMap = new HashMap<>();
                for (Comment comment : comments) {
                    String createBy = comment.getCreateBy();
                    UserVo userVo = userVoMap.get(createBy);
                    if (userVo != null) {
                        comment.setNickname(userVo.getNickname());
                        comment.setAvatar(userVo.getAvatar());
                    }
                    commentMap.put(comment.getId(), comment);
                }
                for (Comment comment : comments) {
                    Integer parentId = comment.getParentId();
                    if (parentId != null) {
                        Comment parentComment = commentMap.get(parentId);
                        if (parentComment != null) {
                            comment.setParentNickname(parentComment.getNickname());
                        }
                    }
                }
                List<CommentVo> commentVos = TreeUtils.convertToTwoLevelTree(comments);
                articleVo.setCommentVos(commentVos);
            }
            UserVo userVo = systemOpenfeign.getUserByUsername(record.getCreateBy());
            articleVo.setUserVo(userVo);
            List<ArticleFile> articleFiles = articleFileService.listByArticleId(id);
            articleVo.setArticleFiles(articleFiles);
            articleVos.add(articleVo);
        }
        return articleVos;
    }
}
