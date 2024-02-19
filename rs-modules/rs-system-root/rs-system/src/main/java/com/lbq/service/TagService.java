package com.lbq.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lbq.pojo.Tag;
import com.lbq.vo.PageVo;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 标签
 *
 * @author lbq
 * @since 2024-01-30
 */
public interface TagService extends IService<Tag> {

    Page<Tag> page(PageVo pageVo, String keyword);

    void add(Tag tag);

    void edit(Tag tag);

    void delete(Tag tag);

    void enable(Tag tag);

    void disable(Tag tag);
}
