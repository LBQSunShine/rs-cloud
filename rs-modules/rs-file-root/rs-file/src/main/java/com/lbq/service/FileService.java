package com.lbq.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件
 *
 * @Author: lbq
 * @Date: 2024/2/19
 * @Version: 1.0
 */
public interface FileService {
    String upload(MultipartFile file) throws IOException;
}
