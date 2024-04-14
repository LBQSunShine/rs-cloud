package com.lbq.openfeign;


import com.lbq.service.ArticleFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 标签
 *
 * @author lbq
 * @since 2024-04-14
 */
@RestController
@RequestMapping("/article/openfeign/file")
public class ArticleFileOpenfeign {

    @Autowired
    private ArticleFileService articleFileService;

    @GetMapping("/getArticleFiles")
    public List<String> getArticleFiles() {
        return articleFileService.getArticleFiles();
    }
}

