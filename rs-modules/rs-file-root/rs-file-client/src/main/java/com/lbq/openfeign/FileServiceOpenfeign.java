package com.lbq.openfeign;

import com.lbq.config.OpenfeignFormConfig;
import com.lbq.vo.FileVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
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
@FeignClient(value = "file", configuration = OpenfeignFormConfig.class)
public interface FileServiceOpenfeign {
    @PostMapping(value = "/file/openfeign/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileVo upload(MultipartFile file);

    @PostMapping("/file/openfeign/copyToProd")
    void copyToProd(List<FileVo> fileVos);

    @PostMapping("/file/openfeign/deleteFile")
    void deleteFile();
}
