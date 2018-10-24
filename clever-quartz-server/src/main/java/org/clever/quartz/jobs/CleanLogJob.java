package org.clever.quartz.jobs;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.DateTimeUtils;
import org.clever.common.utils.spring.SpringContextHolder;
import org.clever.quartz.service.CleanLogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.Date;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-31 13:12 <br/>
 */
@Slf4j
public class CleanLogJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        CleanLogService cleanLogService = SpringContextHolder.getBean(CleanLogService.class);
        assert cleanLogService != null;
        Date date = DateTimeUtils.addMonths(new Date(), -1);
        cleanLogService.cleanLog(date);
    }
}
