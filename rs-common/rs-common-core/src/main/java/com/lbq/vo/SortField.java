package com.lbq.vo;

import lombok.Data;

/**
 * 排序参数
 *
 * @Author lbq
 * @Date 2024/2/1
 * @Version: 1.0
 */
@Data
public class SortField {
    private String sortField;
    private String sortType;
    public static String ASC = "asc";
    public static String DESC = "desc";
}
