package com.lbq.vo;

import lombok.Data;

/**
 * 分页参数
 *
 * @Author lbq
 * @Date 2024/2/1
 * @Version: 1.0
 */
@Data
public class PageVo extends SortField {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
}
