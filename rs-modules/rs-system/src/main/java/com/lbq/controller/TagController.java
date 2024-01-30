package com.lbq.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lbq.constants.StatusConstants;
import com.lbq.function.ActionFunction;
import com.lbq.pojo.Tag;
import com.lbq.service.TagService;
import com.lbq.utils.IdsReq;
import com.lbq.utils.StringFormatUtils;
import com.lbq.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 标签
 *
 * @author lbq
 * @since 2024-01-30
 */
@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public R<?> list(@RequestParam(name = "isAll") boolean isAll) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getStatus, StatusConstants.ENABLE);
        if (!isAll) {
            queryWrapper.last("limit 9");
        }
        List<Tag> list = tagService.list(queryWrapper);
        return R.success(list);
    }


    @PostMapping("/add")
    public R<?> add(@RequestBody Tag tag) {
        tagService.add(tag);
        return R.success("新增成功!");
    }

    @PostMapping("/edit")
    public R<?> edit(@RequestBody Tag tag) {
        tagService.edit(tag);
        return R.success("编辑成功!");
    }

    @PostMapping("/delete")
    public R<?> delete(@RequestBody IdsReq idsReq) {
        List<String> message = this.action(idsReq.getIds(), "删除", (tag) -> {
            tagService.delete(tag);
        });
        return R.success(message);
    }

    @PostMapping("/enable")
    public R<?> enable(@RequestBody IdsReq idsReq) {
        List<String> message = this.action(idsReq.getIds(), "启用", (tag) -> {
            tagService.enable(tag);
        });
        return R.success(message);
    }

    @PostMapping("/disable")
    public R<?> disable(@RequestBody IdsReq idsReq) {
        List<String> message = this.action(idsReq.getIds(), "禁用", (tag) -> {
            tagService.disable(tag);
        });
        return R.success(message);
    }

    private List<String> action(Collection<Integer> ids, String option, ActionFunction<Tag> fun) {
        List<Tag> tags = tagService.listByIds(ids);
        List<String> message = new ArrayList<>();
        for (Tag tag : tags) {
            String code = tag.getCode();
            try {
                fun.callback(tag);
                String format = StringFormatUtils.format("标签编码{}{}成功！", code, option);
                message.add(format);
            } catch (Exception e) {
                String format = StringFormatUtils.format("标签编码{}{}失败！失败原因：{}", code, option, e.getMessage());
                message.add(format);
            }
        }
        return message;
    }
}

