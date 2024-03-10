package com.lbq.openfeign;

import com.lbq.config.OpenfeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Author lbq
 * @Date 2024/3/10
 * @Version: 1.0
 */
@Component
@FeignClient(value = "article", configuration = OpenfeignConfig.class)
public interface ArticleOpenfeign {

    @PostMapping("/article/openfeign/saveArticleUpvote")
    void saveArticleUpvote();
}
