package org.clever.quartz.utils;

import org.clever.quartz.dto.response.CurrentlyExecutingJobsRes;
import org.clever.quartz.dto.response.JobDetailInfoRes;
import org.clever.quartz.dto.response.JobDetailsRes;
import org.clever.quartz.dto.response.TriggerInfoRes;
import org.clever.quartz.entity.QrtzJobDetails;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.impl.triggers.CalendarIntervalTriggerImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.DailyTimeIntervalTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-10 11:46 <br/>
 */
@SuppressWarnings("WeakerAccess")
public class ConvertUtils {

    private static boolean convert(String str) {
        if (str != null && str.length() > 0) {
            int c = Character.toLowerCase(str.charAt(0));
            return (c == 't' || c == 'y' || c == '1' || str.equals("-1"));
        }
        return false;
    }

    public static JobDetailsRes convert(QrtzJobDetails qrtzJobDetails) {
        JobDetailsRes jobDetailsRes = new JobDetailsRes();
        jobDetailsRes.setSchedName(qrtzJobDetails.getSchedName());
        jobDetailsRes.setJobName(qrtzJobDetails.getJobName());
        jobDetailsRes.setJobGroup(qrtzJobDetails.getJobGroup());
        jobDetailsRes.setDescription(qrtzJobDetails.getDescription());
        jobDetailsRes.setJobClassName(qrtzJobDetails.getJobClassName());
        jobDetailsRes.setDurable(convert(qrtzJobDetails.getIsDurable()));
        jobDetailsRes.setNonconcurrent(convert(qrtzJobDetails.getIsNonconcurrent()));
        jobDetailsRes.setUpdateData(convert(qrtzJobDetails.getIsUpdateData()));
        jobDetailsRes.setRequestsRecovery(convert(qrtzJobDetails.getRequestsRecovery()));
        return jobDetailsRes;
    }

    public static List<JobDetailsRes> convert(List<QrtzJobDetails> qrtzJobDetailsList) {
        if (qrtzJobDetailsList == null) {
            return null;
        }
        List<JobDetailsRes> jobDetailsResList = new ArrayList<>();
        for (QrtzJobDetails qrtzJobDetails : qrtzJobDetailsList) {
            jobDetailsResList.add(convert(qrtzJobDetails));
        }
        return jobDetailsResList;
    }

    public static CurrentlyExecutingJobsRes convert(String schedName, JobExecutionContext jobExecutionContext) {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        CurrentlyExecutingJobsRes currentlyExecutingJobsRes = new CurrentlyExecutingJobsRes();
        currentlyExecutingJobsRes.setSchedName(schedName);
        currentlyExecutingJobsRes.setJobGroup(jobDetail.getKey().getGroup());
        currentlyExecutingJobsRes.setJobName(jobDetail.getKey().getName());
        currentlyExecutingJobsRes.setDurable(jobDetail.isDurable());
        currentlyExecutingJobsRes.setDescription(jobDetail.getDescription());
        currentlyExecutingJobsRes.setJobClassName(jobDetail.getJobClass().getName());
        currentlyExecutingJobsRes.setRequestsRecovery(jobDetail.requestsRecovery());
        // @DisallowConcurrentExecution 对应 isNonconcurrent
        // @PersistJobDataAfterExecution 对应 isUpdateData
        currentlyExecutingJobsRes.setNonconcurrent(jobDetail.isConcurrentExectionDisallowed());
        currentlyExecutingJobsRes.setUpdateData(jobDetail.isPersistJobDataAfterExecution());
        currentlyExecutingJobsRes.setRecovering(jobExecutionContext.isRecovering());
        currentlyExecutingJobsRes.setRefireCount(jobExecutionContext.getRefireCount());
        currentlyExecutingJobsRes.setJobRunTime(jobExecutionContext.getJobRunTime());
        currentlyExecutingJobsRes.setFireTime(jobExecutionContext.getFireTime());
        currentlyExecutingJobsRes.setNextFireTime(jobExecutionContext.getNextFireTime());
        currentlyExecutingJobsRes.setJobData(jobDetail.getJobDataMap());
        currentlyExecutingJobsRes.setTrigger(jobExecutionContext.getTrigger());
        currentlyExecutingJobsRes.setCalendar(jobExecutionContext.getCalendar());
        return currentlyExecutingJobsRes;
    }

    public static List<CurrentlyExecutingJobsRes> convert(String schedName, List<JobExecutionContext> jobExecutionContextList) {
        if (jobExecutionContextList == null) {
            return null;
        }
        List<CurrentlyExecutingJobsRes> currentlyExecutingJobsResList = new ArrayList<>();
        for (JobExecutionContext jobExecutionContext : jobExecutionContextList) {
            currentlyExecutingJobsResList.add(convert(schedName, jobExecutionContext));
        }
        return currentlyExecutingJobsResList;
    }

    public static TriggerInfoRes convert(String schedName, Trigger trigger, Trigger.TriggerState state) {
        TriggerInfoRes triggerInfoRes = new TriggerInfoRes();
        triggerInfoRes.setSchedName(schedName);
        triggerInfoRes.setTriggerName(trigger.getKey().getName());
        triggerInfoRes.setTriggerGroup(trigger.getKey().getGroup());
        triggerInfoRes.setJobName(trigger.getJobKey().getName());
        triggerInfoRes.setJobGroup(trigger.getJobKey().getGroup());
        triggerInfoRes.setDescription(trigger.getDescription());
        triggerInfoRes.setNextFireTime(trigger.getNextFireTime());
        triggerInfoRes.setPrevFireTime(trigger.getPreviousFireTime());
        triggerInfoRes.setPriority(trigger.getPriority());
        triggerInfoRes.setTriggerState(state.name());
        triggerInfoRes.setTriggerType(trigger.getClass().getName());
        triggerInfoRes.setStartTime(trigger.getStartTime());
        triggerInfoRes.setEndTime(trigger.getEndTime());
        triggerInfoRes.setCalendarName(trigger.getCalendarName());
        triggerInfoRes.setMisfireInstr(trigger.getMisfireInstruction());
        triggerInfoRes.setJobData(trigger.getJobDataMap());
        if (trigger instanceof SimpleTriggerImpl) {
            SimpleTriggerImpl simpleTrigger = (SimpleTriggerImpl) trigger;
            triggerInfoRes.setRepeatCount(simpleTrigger.getRepeatCount());
            triggerInfoRes.setRepeatInterval(simpleTrigger.getRepeatInterval());
            triggerInfoRes.setTimesTriggered(simpleTrigger.getTimesTriggered());
        }
        if (trigger instanceof CronTriggerImpl) {
            CronTriggerImpl cronTrigger = (CronTriggerImpl) trigger;
            triggerInfoRes.setCronEx(cronTrigger.getCronExpression());
            triggerInfoRes.setTimeZoneId(cronTrigger.getTimeZone() == null ? null : cronTrigger.getTimeZone().getID());
        }
        //noinspection StatementWithEmptyBody
        if (trigger instanceof CalendarIntervalTriggerImpl) {
            // TODO CalendarIntervalTriggerImpl
        }
        //noinspection StatementWithEmptyBody
        if (trigger instanceof DailyTimeIntervalTriggerImpl) {
            // TODO CalendarIntervalTriggerImpl
        }
        return triggerInfoRes;
    }

    public static JobDetailInfoRes convert(String schedName, JobDetail jobDetail) {
        JobDetailInfoRes jobDetailInfoRes = new JobDetailInfoRes();
        jobDetailInfoRes.setSchedName(schedName);
        jobDetailInfoRes.setJobGroup(jobDetail.getKey().getGroup());
        jobDetailInfoRes.setJobName(jobDetail.getKey().getName());
        jobDetailInfoRes.setDurable(jobDetail.isDurable());
        jobDetailInfoRes.setDescription(jobDetail.getDescription());
        jobDetailInfoRes.setJobClassName(jobDetail.getJobClass().getName());
        jobDetailInfoRes.setJobData(jobDetail.getJobDataMap());
        jobDetailInfoRes.setRequestsRecovery(jobDetail.requestsRecovery());
        // @DisallowConcurrentExecution 对应 isNonconcurrent
        // @PersistJobDataAfterExecution 对应 isUpdateData
        jobDetailInfoRes.setNonconcurrent(jobDetail.isConcurrentExectionDisallowed());
        jobDetailInfoRes.setUpdateData(jobDetail.isPersistJobDataAfterExecution());
        return jobDetailInfoRes;
    }
}
