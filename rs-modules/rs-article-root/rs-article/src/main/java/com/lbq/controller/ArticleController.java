package com.lbq.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lbq.pojo.Article;
import com.lbq.pojo.Comment;
import com.lbq.service.ArticleService;
import com.lbq.utils.IdsReq;
import com.lbq.utils.StringFormatUtils;
import com.lbq.vo.ArticleVo;
import com.lbq.vo.PageVo;
import com.lbq.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public R<?> page(PageVo pageVo,
                     @RequestParam(name = "keyword", required = false) String keyword,
                     @RequestParam(name = "tagIds", required = false) String tagIds,
                     @RequestParam(name = "selectType", required = false) String selectType) {
        List<Integer> tIds = StringFormatUtils.stringToList(tagIds, ",");
        Page<ArticleVo> res = articleService.page(pageVo, keyword, selectType, tIds);
        return R.success(res);
    }

    @GetMapping("/getById")
    public R<?> getById(@RequestParam(name = "id") Integer id) {
        ArticleVo res = articleService.getById(id);
        return R.success(res);
    }

    @PostMapping("/add")
    public R<?> add(@RequestBody ArticleVo articleVo) {
        articleService.add(articleVo);
        return R.success("发布成功!");
    }

    @PostMapping("/edit")
    public R<?> edit(@RequestBody ArticleVo articleVo) {
        articleService.edit(articleVo);
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

    @PostMapping("/comment")
    public R<?> comment(@RequestBody Comment comment) {
        articleService.comment(comment);
        return R.success("发布成功!");
    }

    @PostMapping("/deleteComment")
    public R<?> deleteComment(@RequestBody IdsReq idsReq) {
        articleService.deleteComment(idsReq.getIds());
        return R.success("删除成功!");
    }

    @PostMapping("/upload")
    public R<?> upload(MultipartFile file) {
        String upload = articleService.upload(file);
        return R.success("上传成功!", upload);
    }
}

