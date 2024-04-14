package com.lbq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.mapper.FileMapper;
import com.lbq.openfeign.ArticleOpenfeign;
import com.lbq.openfeign.SystemOpenfeign;
import com.lbq.pojo.RsFile;
import com.lbq.service.FileService;
import com.lbq.vo.FileVo;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author: lbq
 * @Date: 2024/2/19
 * @Version: 1.0
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, RsFile> implements FileService {

    @Value("${filePath}")
    private String filePath;

    @Autowired
    private SystemOpenfeign systemOpenfeign;

    @Autowired
    private ArticleOpenfeign articleOpenfeign;

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
        String fileUrl = ("/file/preview/" + format + "/" + fileName).replace("\\", "/");
        RsFile rsFile = new RsFile();
        rsFile.setUrl(fileUrl);
        rsFile.setCreateTime(new Date());
        super.save(rsFile);
        return fileUrl;
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

    @Override
    public void deleteFile() {
        LambdaQueryWrapper<RsFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.lt(RsFile::getCreateTime, new Date());
        List<RsFile> rsFiles = super.list(queryWrapper);
        if (CollectionUtils.isEmpty(rsFiles)) {
            return;
        }
        List<String> allFiles = rsFiles.stream().map(RsFile::getUrl).collect(Collectors.toList());
        // 获取其他微服务中使用到的文件
        List<String> allInUserFiles = new ArrayList<>();
        List<String> systemInUserFiles = systemOpenfeign.getUserFiles();
        List<String> articleFiles = articleOpenfeign.getArticleFiles();
        allInUserFiles.addAll(systemInUserFiles);
        allInUserFiles.addAll(articleFiles);
        // 找出差集：allFiles中存在但inUseFiles中不存在的元素
        List<String> difference = allFiles.stream()
                .filter(item -> !allInUserFiles.contains(item))
                .collect(Collectors.toList());
        // 遍历文件夹中的所有文件，检查它们是否存在于列表中
        for (String deleteFile : difference) {
            String replace = deleteFile.replace("/file/preview/", filePath).replace("/", "\\");
            Path path = Paths.get(replace);
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
