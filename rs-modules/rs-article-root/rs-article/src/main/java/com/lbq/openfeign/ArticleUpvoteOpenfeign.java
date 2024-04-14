package com.lbq.openfeign;


import com.lbq.service.ArticleUpvoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 定时保存点赞
 *
 * @author lbq
 * @since 2024-03-10
 */
@RestController
@RequestMapping("/article/openfeign/upvote")
public class ArticleUpvoteOpenfeign {

    @Autowired
    private ArticleUpvoteService articleUpvoteService;

    @PostMapping("/saveArticleUpvote")
    public void saveArticleUpvote() {
        articleUpvoteService.saveArticleUpvote();
    }
}

