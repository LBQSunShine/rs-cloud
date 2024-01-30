package com.lbq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbq.pojo.Tag;

/**
 * 标签
 *
 * @author lbq
 * @since 2024-01-30
 */
public interface TagService extends IService<Tag> {

    void add(Tag tag);

    void edit(Tag tag);

    void delete(Tag tag);

    void enable(Tag tag);

    void disable(Tag tag);
}
