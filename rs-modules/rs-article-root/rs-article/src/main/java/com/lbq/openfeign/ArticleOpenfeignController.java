package com.lbq.openfeign;


import com.lbq.service.ArticleFileService;
import com.lbq.service.ArticleUpvoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ArticleOpenfeignApi
 *
 * @author lbq
 * @since 2024-04-14
 */
@RestController
@RequestMapping("/article/openfeign")
public class ArticleOpenfeignController {

    @Autowired
    private ArticleFileService articleFileService;

    @Autowired
    private ArticleUpvoteService articleUpvoteService;

    @GetMapping("/getArticleFiles")
    public List<String> getArticleFiles() {
        return articleFileService.getArticleFiles();
    }

    @PostMapping("/saveArticleUpvote")
    public void saveArticleUpvote() {
        articleUpvoteService.saveArticleUpvote();
    }
}

