package org.clever.quartz.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.model.response.AjaxMessage;
import org.clever.common.utils.exception.ExceptionUtils;
import org.clever.quartz.dto.request.AddHttpServiceJobReq;
import org.clever.quartz.jobs.HttpServiceJob;
import org.clever.quartz.model.HttpJobData;
import org.clever.quartz.model.HttpJobNotice;
import org.clever.quartz.utils.QuartzManager;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/6/5 13:54 <br/>
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class HttpServiceJobService {

    /**
     * 保存 Http任务
     */
    @Transactional
    public boolean addHttpServiceJob(AddHttpServiceJobReq addHttpServiceJobReq, AjaxMessage ajaxMessage) {
        Scheduler scheduler = QuartzManager.getScheduler();
        JobDetail jobDetail;
        try {
            jobDetail = scheduler.getJobDetail(JobKey.jobKey(addHttpServiceJobReq.getName(), addHttpServiceJobReq.getGroup()));
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw ExceptionUtils.unchecked(e);
        }
        if (jobDetail != null) {
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("新增任务失败,任务已经存在");
            return false;
        }

        // ----------------------------------------------------新增JobDetail
        JobBuilder jobBuilder = JobBuilder.newJob(HttpServiceJob.class);
        jobBuilder.withIdentity(addHttpServiceJobReq.getName(), addHttpServiceJobReq.getGroup());
        // 需要存储的job必须调用此方法
        jobBuilder.storeDurably();
        jobBuilder.withDescription(addHttpServiceJobReq.getDescription());
        jobBuilder.requestRecovery(addHttpServiceJobReq.isRequestsRecovery());
        // @DisallowConcurrentExecution 对应 isNonconcurrent
        // @PersistJobDataAfterExecution 对应 isUpdateData
        jobDetail = jobBuilder.build();
        jobDetail.getJobDataMap().put(HttpJobData.HTTP_JOB_DATA_KEY, addHttpServiceJobReq.getHttpJobData());
        jobDetail.getJobDataMap().put(HttpJobNotice.HTTP_JOB_NOTICE_KEY, addHttpServiceJobReq.getNotice());
        jobDetail.getJobDataMap().put("jobExtendData", addHttpServiceJobReq.getJobExtendData());
        try {
            scheduler.addJob(jobDetail, false);
        } catch (Throwable e) {
            log.error("新增HttpServiceJob失败", e);
            throw ExceptionUtils.unchecked(e);
        }

        // ----------------------------------------------------新增Trigger
        TriggerBuilder<Trigger> triggerBuilder = QuartzTriggerService.newTriggerBuilder(
                addHttpServiceJobReq.getName(),
                addHttpServiceJobReq.getGroup(),
                addHttpServiceJobReq.getName(),
                addHttpServiceJobReq.getGroup(),
                addHttpServiceJobReq.getDescription(),
                addHttpServiceJobReq.getStartTime(),
                addHttpServiceJobReq.getEndTime(),
                addHttpServiceJobReq.getPriority(),
                null);
        CronScheduleBuilder cronScheduleBuilder = QuartzTriggerService.newCronScheduleBuilder(
                addHttpServiceJobReq.getCron(),
                addHttpServiceJobReq.getMisfireInstruction());
        triggerBuilder.withSchedule(cronScheduleBuilder);
        Trigger trigger = triggerBuilder.build();
        try {
            scheduler.scheduleJob(trigger);
        } catch (Throwable e) {
            log.error("新增HttpServiceJob失败", e);
            throw ExceptionUtils.unchecked(e);
        }
        return true;
    }
}
