package com.lbq.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 文章点赞
 *
 * @author lbq
 * @since 2024-03-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("rs_article_upvote")
public class ArticleUpvote implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String existsKey;

    private Integer articleId;

    private String status;

    private String upvoteBy;

    public ArticleUpvote(String upvoteBy, String articleId, String status, String existsKey) {
        this.upvoteBy = upvoteBy;
        this.articleId = Integer.valueOf(articleId);
        this.status = status;
        this.existsKey = existsKey;
    }
}
