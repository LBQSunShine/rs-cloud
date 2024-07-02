package com.lbq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
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
            List<ArticleUpvote> articleUpvoteList = new ArrayList<>();
            for (Map<String, String> item : mapList) {
                Iterator<Map.Entry<String, String>> iterator = item.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> next = iterator.next();
                    String key = next.getKey();
                    String[] split = key.split("::");
                    String value = next.getValue();
                    ArticleUpvote articleUpvote = new ArticleUpvote(split[0], split[1], value, key);
                    articleUpvoteList.add(articleUpvote);
                }
                if (CollectionUtils.isNotEmpty(articleUpvoteList)) {
                    articleUpvoteMapper.saveArticleUpvote(articleUpvoteList);
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
}
