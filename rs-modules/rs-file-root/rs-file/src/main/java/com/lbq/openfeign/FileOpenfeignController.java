package com.lbq.openfeign;

import com.lbq.service.FileService;
import com.lbq.vo.FileVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件
 *
 * @Author: lbq
 * @Date: 2024/2/19
 * @Version: 1.0
 */
@RestController
@RequestMapping("/file/openfeign")
public class FileOpenfeignController {

    @Autowired
    private FileService fileService;

    @PostMapping(value = "/upload")
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

    @PostMapping("/copyToProd")
    public void copyToProd(@RequestBody List<FileVo> fileVos) {
        try {
            fileService.copyToProd(fileVos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/deleteFile")
    public void deleteFile() {
        try {
            fileService.deleteFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
