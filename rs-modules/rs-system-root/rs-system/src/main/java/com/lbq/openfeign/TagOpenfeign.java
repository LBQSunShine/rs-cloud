package com.lbq.openfeign;


import com.lbq.service.TagService;
import com.lbq.utils.IdsReq;
import com.lbq.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 标签
 *
 * @author lbq
 * @since 2024-02-25
 */
@RestController
@RequestMapping("/sys/openfeign/tag")
public class TagOpenfeign {

    @Autowired
    private TagService tagService;

    @PostMapping("/getMapByIds")
    public Map<Integer, TagVo> getMapByIds(@RequestBody IdsReq idsReq) {
        return tagService.getMapByIds(idsReq.getIds());
    }
}

