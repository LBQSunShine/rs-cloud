package com.lbq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.constants.StatusConstants;
import com.lbq.context.BaseContext;
import com.lbq.mapper.TagMapper;
import com.lbq.pojo.Tag;
import com.lbq.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 标签
 *
 * @author lbq
 * @since 2024-01-30
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public void add(Tag tag) {
        tag.setCreateBy(BaseContext.getUsername());
        tag.setCreateTime(new Date());
        tag.setStatus(StatusConstants.ENABLE);
        int add = tagMapper.add(tag);
        if (add != 1) {
            throw new RuntimeException("标签已存在!");
        }
    }

    @Override
    public void edit(Tag tag) {
        LambdaUpdateWrapper<Tag> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(Tag::getId, tag.getId())
                .eq(Tag::getStatus, StatusConstants.ENABLE)
                .set(Tag::getName, tag.getName())
                .set(Tag::getUpdateBy, BaseContext.getUsername())
                .set(Tag::getUpdateTime, new Date());
        boolean update = super.update(updateWrapper);
        if (!update) {
            throw new RuntimeException("状态变更，请刷新重试!");
        }
    }

    @Override
    public void delete(Tag tag) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Tag::getId, tag.getId())
                .eq(Tag::getStatus, StatusConstants.ENABLE);
        boolean remove = super.remove(queryWrapper);
        if (!remove) {
            throw new RuntimeException("状态变更，请刷新重试!");
        }
    }

    @Override
    public void enable(Tag tag) {
        LambdaUpdateWrapper<Tag> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(Tag::getId, tag.getId())
                .eq(Tag::getStatus, StatusConstants.DISABLE)
                .set(Tag::getStatus, StatusConstants.ENABLE)
                .set(Tag::getUpdateBy, BaseContext.getUsername())
                .set(Tag::getUpdateTime, new Date());
        boolean update = super.update(updateWrapper);
        if (!update) {
            throw new RuntimeException("状态变更，请刷新重试!");
        }
    }

    @Override
    public void disable(Tag tag) {
        LambdaUpdateWrapper<Tag> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(Tag::getId, tag.getId())
                .eq(Tag::getStatus, StatusConstants.ENABLE)
                .set(Tag::getStatus, StatusConstants.DISABLE)
                .set(Tag::getUpdateBy, BaseContext.getUsername())
                .set(Tag::getUpdateTime, new Date());
        boolean update = super.update(updateWrapper);
        if (!update) {
            throw new RuntimeException("状态变更，请刷新重试!");
        }
    }
}
