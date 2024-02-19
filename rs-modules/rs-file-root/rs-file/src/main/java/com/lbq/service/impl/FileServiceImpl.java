package com.lbq.service.impl;

import com.lbq.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: lbq
 * @Date: 2024/2/19
 * @Version: 1.0
 */
@Service
public class FileServiceImpl implements FileService {

    @Override
    public String upload(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("文件为空!");
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String format = dateFormat.format(date);
        String filePath = "D:\\Tools\\upload\\"; // 上传后的路径
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        fileName = uuid + suffixName; // 新文件名
        String url = filePath + format + "\\" + fileName;
        File dest = new File(url);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest);
        return url;
    }
}
