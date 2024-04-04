package com.lbq.openfeign;

import com.lbq.vo.FileVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * openfeign
 *
 * @Author: lbq
 * @Date: 2024/2/19
 * @Version: 1.0
 */
@Component
@FeignClient(value = "file")
public interface FileServiceOpenfeign {
    @PostMapping("/file/openfeign/upload")
    FileVo upload(MultipartFile file);

    @PostMapping("/file/openfeign/copyToProd")
    void copyToProd(List<FileVo> fileVos);
}
