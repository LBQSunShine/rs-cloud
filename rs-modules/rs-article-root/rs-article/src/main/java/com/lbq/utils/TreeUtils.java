package com.lbq.utils;

import com.lbq.pojo.Comment;
import com.lbq.vo.CommentVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 树形结构工具
 *
 * @Author lbq
 * @Date 2024/4/4
 * @Version: 1.0
 */
public class TreeUtils {

    public static List<CommentVo> convertToTree(List<Comment> comments) {
        // 创建一个Map来存储每个Tree对象，以id为key
        Map<Integer, Comment> treeMap = comments.stream()
                .collect(Collectors.toMap(Comment::getId, tree -> tree, (existing, replacement) -> existing));

        // 创建一个列表来存储根节点
        List<CommentVo> rootNodes = new ArrayList<>();

        // 遍历所有的Tree对象
        for (Comment comment : comments) {
            // 检查当前Tree是否有parentId（即它是否是一个子节点）
            if (comment.getParentId() == null) {
                // 如果没有parentId，那么它是一个根节点
                CommentVo rootVo = convertToTreeVo(comment, treeMap);
                rootNodes.add(rootVo);
            }
        }
        return rootNodes;
    }

    private static CommentVo convertToTreeVo(Comment comment, Map<Integer, Comment> treeMap) {
        CommentVo commentVo = new CommentVo(comment);
        List<Comment> children = treeMap.entrySet().stream()
                .filter(entry -> entry.getValue().getParentId() == comment.getId()) // 找到所有子节点
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        for (Comment child : children) {
            commentVo.getChildren().add(convertToTreeVo(child, treeMap)); // 递归转换子节点
        }
        return commentVo;
    }
}