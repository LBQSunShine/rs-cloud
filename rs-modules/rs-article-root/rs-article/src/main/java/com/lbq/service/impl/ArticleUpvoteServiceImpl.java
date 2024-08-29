package com.lbq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.constants.ArticleConstants;
import com.lbq.constants.StatusConstants;
import com.lbq.mapper.ArticleUpvoteMapper;
import com.lbq.pojo.ArticleUpvote;
import com.lbq.service.ArticleUpvoteService;
import com.lbq.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 文章点赞
 *
 * @author lbq
 * @since 2024-03-10
 */
@Service
public class ArticleUpvoteServiceImpl extends ServiceImpl<ArticleUpvoteMapper, ArticleUpvote> implements ArticleUpvoteService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ArticleUpvoteMapper articleUpvoteMapper;

    @Override
    @Transactional
    public void saveArticleUpvote() {
        List<Map<String, String>> mapList = redisService.hGetValuesWithPrefix(ArticleConstants.UPVOTE);
        try {
            for (Map<String, String> item : mapList) {
                Iterator<Map.Entry<String, String>> iterator = item.entrySet().iterator();
                String k = "";
                List<String> ks = new ArrayList<>();
                List<ArticleUpvote> articleUpvoteList = new ArrayList<>();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> next = iterator.next();
                    String key = next.getKey();
                    String[] split = key.split("::");
                    String value = next.getValue();
                    if (StringUtils.isBlank(k)) {
                        k = ArticleConstants.UPVOTE + split[0];
                    }
                    ks.add(key);
                    ArticleUpvote articleUpvote = new ArticleUpvote(split[0], split[1], value, key);
                    articleUpvoteList.add(articleUpvote);
                }
                if (CollectionUtils.isNotEmpty(articleUpvoteList)) {
                    articleUpvoteMapper.saveArticleUpvote(articleUpvoteList);
                }
                for (String kItem : ks) {
                    redisService.hDeleteValue(k, kItem);
                }
                this.removeUnUpvote();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<ArticleUpvote> listByArticleId(Integer articleId) {
        LambdaQueryWrapper<ArticleUpvote> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ArticleUpvote::getArticleId, articleId)
                .eq(ArticleUpvote::getStatus, StatusConstants.STATUS_1);
        return super.list(queryWrapper);
    }

    @Override
    public void removeUnUpvote() {
        LambdaQueryWrapper<ArticleUpvote> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleUpvote::getStatus, StatusConstants.STATUS_0);
        super.remove(queryWrapper);
    }

    @Override
    public void removeByArticleIds(Collection<Integer> articleIds) {
        if (CollectionUtils.isEmpty(articleIds)) {
            return;
        }
        LambdaQueryWrapper<ArticleUpvote> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ArticleUpvote::getArticleId, articleIds);
        super.remove(queryWrapper);
    }

    @Override
    public int countByArticleId(Integer articleId) {
        LambdaQueryWrapper<ArticleUpvote> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ArticleUpvote::getArticleId, articleId)
                .eq(ArticleUpvote::getStatus, StatusConstants.STATUS_1);
        return (int) super.count(queryWrapper);
    }

    @Override
    public boolean isUpvoteByArticleIdAndUsername(Integer articleId, String username) {
        String key = ArticleConstants.UPVOTE + username;
        String hKey = username + "::" + articleId;
        String status = redisService.hGetValue(key, hKey);
        if (StatusConstants.STATUS_1.equals(status)) {
            return true;
        } else if (StatusConstants.STATUS_0.equals(status)) {
            return false;
        }
        LambdaQueryWrapper<ArticleUpvote> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ArticleUpvote::getArticleId, articleId)
                .eq(ArticleUpvote::getUpvoteBy, username)
                .eq(ArticleUpvote::getStatus, StatusConstants.STATUS_1);
        return super.count(queryWrapper) > 0;
    }
}
