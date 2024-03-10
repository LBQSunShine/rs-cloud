package com.lbq.service.impl;

import com.lbq.pojo.Article;
import com.lbq.mapper.ArticleMapper;
import com.lbq.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 文章
 *
 * @author lbq
 * @since 2024-03-10
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

}
