package com.lbq.job;

import com.lbq.openfeign.ArticleOpenfeign;
import com.lbq.utils.SpringContextUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 定时同步点赞数据任务
 *
 * @Author lbq
 * @Date 2024/3/31
 * @Version: 1.0
 */
public class SaveArticleUpvoteJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ArticleOpenfeign articleOpenfeign = SpringContextUtils.getBean(ArticleOpenfeign.class);
        articleOpenfeign.saveArticleUpvote();
    }
}
