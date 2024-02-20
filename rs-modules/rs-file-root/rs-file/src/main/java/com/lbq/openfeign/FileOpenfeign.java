package com.lbq.openfeign;

import com.lbq.service.FileService;
import com.lbq.vo.FileVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件
 *
 * @Author: lbq
 * @Date: 2024/2/19
 * @Version: 1.0
 */
@RestController
@RequestMapping("/file/openfeign")
public class FileOpenfeign {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public FileVo upload(MultipartFile file) {
        try {
            String url = fileService.upload(file);
            FileVo fileVo = new FileVo();
            fileVo.setUrl(url);
            return fileVo;
        } catch (Exception e) {
            return new FileVo();
        }
    }
}
