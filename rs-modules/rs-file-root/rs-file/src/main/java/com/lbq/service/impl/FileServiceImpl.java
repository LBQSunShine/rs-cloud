package com.lbq.service.impl;

import com.lbq.service.FileService;
import com.lbq.vo.FileVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: lbq
 * @Date: 2024/2/19
 * @Version: 1.0
 */
@Service
public class FileServiceImpl implements FileService {

    @Value("${filePath}")
    private String filePath;

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
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        fileName = uuid + suffixName; // 新文件名
        String url = filePath + format + "\\" + fileName;
        File dest = new File(url);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest);
        String fileUrl = "/file/preview/" + format + "/" + fileName;
        return fileUrl.replace("\\", "/");
    }

    @Override
    public void copyToProd(List<FileVo> fileVos) {
        String prodFilePath = filePath.replaceAll("dev", "prod");
        for (FileVo fileVo : fileVos) {
            Path sourceFile = Paths.get(fileVo.getUrl());
            Path destinationDirectory = Paths.get(prodFilePath);
            Path destinationFile = destinationDirectory.resolve(sourceFile.getFileName());
            // 检查目标文件夹是否存在，如果不存在则创建它
            if (!Files.exists(destinationDirectory)) {
                try {
                    Files.createDirectories(destinationDirectory);
                } catch (IOException e) {
                    throw new RuntimeException("文件夹创建失败!");
                }
            }
            try {
                Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
