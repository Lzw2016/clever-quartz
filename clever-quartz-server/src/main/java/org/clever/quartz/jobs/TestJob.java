package org.clever.quartz.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 23:12 <br/>
 */
@Slf4j
public class TestJob implements Job {

    private static int count = 0;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("### ====================================================== 测试任务 [{}]", count++);
    }
}
