package com.lbq.openfeign;

import com.lbq.config.OpenfeignFormConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @Author lbq
 * @Date 2024/3/10
 * @Version: 1.0
 */
@Component
@FeignClient(value = "article", configuration = OpenfeignFormConfig.class)
public interface ArticleOpenfeign {

    @PostMapping("/article/openfeign/saveArticleUpvote")
    void saveArticleUpvote();

    @GetMapping("/article/openfeign/getArticleFiles")
    List<String> getArticleFiles();

    @PostMapping("/article/openfeign/deleteIsReadMessage")
    void deleteIsReadMessage();
}
