package com.lbq.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.constants.StatusConstants;
import com.lbq.context.BaseContext;
import com.lbq.mapper.TagMapper;
import com.lbq.pojo.Tag;
import com.lbq.service.TagService;
import com.lbq.vo.PageVo;
import com.lbq.vo.SortField;
import com.lbq.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public Page<Tag> page(PageVo pageVo, String keyword) {
        Page<Tag> page = new Page<>(pageVo.getPageNo(), pageVo.getPageSize());
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.lambda().and(wrapper -> {
                wrapper.like(Tag::getCode, keyword)
                        .or()
                        .like(Tag::getName, keyword);
            });
        }
        String sortField = pageVo.getSortField();
        if (StringUtils.isNotBlank(sortField)) {
            String sortType = pageVo.getSortType();
            if (SortField.ASC.equals(sortType)) {
                queryWrapper.orderByAsc(sortField);
            } else {
                queryWrapper.orderByDesc(sortField);
            }
        }
        Page<Tag> res = super.page(page, queryWrapper);
        return res;
    }

    @Override
    @Transactional
    public void add(Tag tag) {
        if (StringUtils.isBlank(tag.getCode())) {
            throw new RuntimeException("请输入编码!");
        }
        if (StringUtils.isBlank(tag.getName())) {
            throw new RuntimeException("请输入名称!");
        }
        tag.setCreateBy(BaseContext.getUsername());
        tag.setCreateTime(new Date());
        tag.setStatus(StatusConstants.ENABLE);
        int add = tagMapper.add(tag);
        if (add != 1) {
            throw new RuntimeException("标签已存在!");
        }
    }

    @Override
    @Transactional
    public void edit(Tag tag) {
        if (StringUtils.isBlank(tag.getCode())) {
            throw new RuntimeException("请输入编码!");
        }
        if (StringUtils.isBlank(tag.getName())) {
            throw new RuntimeException("请输入名称!");
        }
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
    @Transactional
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
    @Transactional
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
    @Transactional
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

    @Override
    public Map<Integer, TagVo> getMapByIds(Collection<Integer> ids) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        if (CollectionUtils.isNotEmpty(ids)) {
            queryWrapper.in(Tag::getId, ids);
        }
        queryWrapper.eq(Tag::getStatus, StatusConstants.ENABLE);
        List<Tag> list = super.list(queryWrapper);
        Map<Integer, TagVo> map = new HashMap<>();
        for (Tag tag : list) {
            Integer id = tag.getId();
            TagVo tagVo = new TagVo();
            tagVo.setId(id);
            tagVo.setCode(tag.getCode());
            tagVo.setName(tag.getName());
            map.put(id, tagVo);
        }
        return map;
    }
}
