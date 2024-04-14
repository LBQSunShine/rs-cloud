package com.lbq.vo;

import com.lbq.pojo.Article;
import com.lbq.pojo.ArticleFile;
import com.lbq.pojo.ArticleUpvote;
import lombok.Data;

import java.util.List;

/**
 * 文章vo
 *
 * @author lbq
 * @since 2024-03-10
 */
@Data
public class ArticleVo {

    private Article article;

    private List<TagVo> tagVos;

    private List<ArticleUpvote> articleUpvotes;

    private List<CommentVo> commentVos;

    private List<ArticleFile> articleFiles;
}
