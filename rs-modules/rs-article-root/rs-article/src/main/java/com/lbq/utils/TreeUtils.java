package com.lbq.utils;

import com.lbq.pojo.Comment;
import com.lbq.vo.CommentVo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 树形结构工具
 *
 * @Author lbq
 * @Date 2024/4/4
 * @Version: 1.0
 */
public class TreeUtils {

    /**
     * 深度优先
     *
     * @param comments
     * @return
     */
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

    /**
     * 广度优先
     *
     * @param comments
     * @return
     */
    public static List<CommentVo> convertToTwoLevelTree(List<Comment> comments) {
        List<CommentVo> result = new ArrayList<>();
        for (Comment comment : comments) {
            CommentVo parentVo = new CommentVo(comment);
            List<CommentVo> children = new ArrayList<>();
            if (comment.getParentId() == null) {
                children = bfs(parentVo, comments);
            } else {
                continue;
            }
            parentVo.setChildren(children);
            result.add(parentVo);
        }
        return result;
    }

    private static List<CommentVo> bfs(CommentVo parentVo, List<Comment> comments) {
        List<CommentVo> children = new ArrayList<>();
        Queue<CommentVo> queue = new LinkedList<>();
        queue.offer(parentVo);
        while (!queue.isEmpty()) {
            CommentVo childVo = queue.poll();
            for (Comment comment : comments) {
                if (comment.getParentId() != null && comment.getParentId() == childVo.getId()) {
                    CommentVo commentVo = new CommentVo(comment);
                    children.add(commentVo);
                    queue.offer(commentVo);
                }
            }
        }
        children.sort(Comparator.comparing(CommentVo::getCreateTime).reversed());
        return children;
    }
}