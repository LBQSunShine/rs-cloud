package com.lbq.vo;

import lombok.Data;

/**
 * 文件信息
 *
 * @Author: lbq
 * @Date: 2024/2/19
 * @Version: 1.0
 */
@Data
public class FileVo {
    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件地址
     */
    private String url;
}
