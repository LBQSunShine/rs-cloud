package com.lbq.job;

import com.lbq.openfeign.ArticleOpenfeign;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 定时同步点赞数据任务
 *
 * @Author lbq
 * @Date 2024/3/31
 * @Version: 1.0
 */
public class SaveArticleUpvoteJob implements Job {

    @Autowired
    private ArticleOpenfeign articleOpenfeign;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        articleOpenfeign.saveArticleUpvote();
    }
}
