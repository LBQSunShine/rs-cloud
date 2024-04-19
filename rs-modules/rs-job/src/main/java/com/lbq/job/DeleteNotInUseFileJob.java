package com.lbq.job;

import com.lbq.openfeign.FileOpenfeign;
import com.lbq.utils.SpringContextUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 定时删除不在使用的文件
 *
 * @Author lbq
 * @Date 2024/3/31
 * @Version: 1.0
 */
public class DeleteNotInUseFileJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        FileOpenfeign fileOpenfeign = SpringContextUtils.getBean(FileOpenfeign.class);
        fileOpenfeign.deleteFile();
    }
}
