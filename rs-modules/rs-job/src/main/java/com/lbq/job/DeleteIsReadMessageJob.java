package com.lbq.job;

import com.lbq.openfeign.ArticleOpenfeign;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 定时删除已读消息
 *
 * @author lbq
 * @since 2024-07-02
 */
public class DeleteIsReadMessageJob implements Job {

    @Autowired
    private ArticleOpenfeign articleOpenfeign;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        articleOpenfeign.deleteIsReadMessage();
    }
}
