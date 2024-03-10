package com.lbq.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.constants.ArticleConstants;
import com.lbq.mapper.ArticleUpvoteMapper;
import com.lbq.pojo.ArticleUpvote;
import com.lbq.service.ArticleUpvoteService;
import com.lbq.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Override
    @Transactional
    public void saveArticleUpvote() {
        Cursor<Map.Entry<Object, Object>> cursor = redisService.hGetValuesWithPrefix(ArticleConstants.UPVOTE);
        List<ArticleUpvote> articleUpvoteList = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> next = cursor.next();
            String key = next.getKey().toString();
            String[] split = key.split("::");
            String value = next.getValue().toString();
            ArticleUpvote articleUpvote = new ArticleUpvote(split[0], split[1], value);
            articleUpvoteList.add(articleUpvote);
        }
        if (CollectionUtils.isNotEmpty(articleUpvoteList)) {
            super.saveOrUpdateBatch(articleUpvoteList);
        }
    }
}
