package com.lbq.vo;

import com.lbq.pojo.Comment;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 评论vo
 *
 * @author lbq
 * @since 2024-03-31
 */
@Data
public class CommentVo {

    private Integer id;

    private Integer parentId;

    private String content;

    private String createBy;

    private Date createTime;

    private List<CommentVo> children;

    public CommentVo(Comment comment) {
        this.id = comment.getId();
        this.parentId = comment.getParentId();
        this.content = comment.getContent();
        this.createBy = comment.getCreateBy();
        this.createTime = comment.getCreateTime();
        this.children = new ArrayList<>();
    }
}
