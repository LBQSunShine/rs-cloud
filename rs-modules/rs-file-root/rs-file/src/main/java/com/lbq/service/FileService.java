package com.lbq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbq.pojo.RsFile;
import com.lbq.vo.FileVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 文件
 *
 * @Author: lbq
 * @Date: 2024/2/19
 * @Version: 1.0
 */
public interface FileService extends IService<RsFile> {
    String upload(MultipartFile file) throws IOException;

    void copyToProd(List<FileVo> fileVos);

    void deleteFile();
}
