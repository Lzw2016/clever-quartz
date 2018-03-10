package org.clever.quartz.utils;

import org.clever.quartz.dto.response.CurrentlyExecutingJobsRes;
import org.clever.quartz.dto.response.JobDetailsRes;
import org.clever.quartz.entity.QrtzJobDetails;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;

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
}
