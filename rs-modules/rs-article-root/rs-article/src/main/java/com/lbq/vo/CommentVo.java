package com.lbq.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lbq.pojo.Comment;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String nickname;

    private String avatar;

    private String parentNickname;

    private List<CommentVo> children;

    public CommentVo(Comment comment) {
        this.id = comment.getId();
        this.parentId = comment.getParentId();
        this.content = comment.getContent();
        this.createBy = comment.getCreateBy();
        this.createTime = comment.getCreateTime();
        this.nickname = comment.getNickname();
        this.avatar = comment.getAvatar();
        this.parentNickname = comment.getParentNickname();
        this.children = new ArrayList<>();
    }
}
