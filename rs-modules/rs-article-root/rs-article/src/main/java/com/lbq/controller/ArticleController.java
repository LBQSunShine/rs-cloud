package com.lbq.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lbq.pojo.Article;
import com.lbq.service.ArticleService;
import com.lbq.utils.IdsReq;
import com.lbq.vo.PageVo;
import com.lbq.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章
 *
 * @author lbq
 * @since 2024-03-10
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/page")
    public R<?> page(PageVo pageVo, @RequestParam(name = "keyword", required = false) String keyword) {
        Page<Article> res = articleService.page(pageVo, keyword);
        return R.success(res);
    }

    @PostMapping("/add")
    public R<?> add(@RequestBody Article article) {
        articleService.add(article);
        return R.success("发布成功!");
    }

    @PostMapping("/edit")
    public R<?> edit(@RequestBody Article article) {
        articleService.edit(article);
        return R.success("修改成功!");
    }

    @PostMapping("/delete")
    public R<?> delete(@RequestBody IdsReq idsReq) {
        articleService.removeByIds(idsReq.getIds());
        return R.success("删除成功!");
    }

    @PostMapping("/upvote")
    public R<?> upvote(@RequestBody Article article) {
        articleService.upvote(article);
        return R.success("点赞成功!");
    }

    @PostMapping("/unUpvote")
    public R<?> unUpvote(@RequestBody Article article) {
        articleService.unUpvote(article);
        return R.success("取消点赞成功!");
    }
}

