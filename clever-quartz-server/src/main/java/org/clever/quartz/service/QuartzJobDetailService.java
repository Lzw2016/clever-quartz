package org.clever.quartz.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.model.response.AjaxMessage;
import org.clever.common.utils.exception.ExceptionUtils;
import org.clever.quartz.dto.request.FindJobDetailRes;
import org.clever.quartz.dto.request.JobDetailKeyRes;
import org.clever.quartz.dto.request.SaveJobDetailRes;
import org.clever.quartz.model.QuartzJobDetails;
import org.clever.quartz.model.QuartzTriggers;
import org.clever.quartz.utils.QuartzManager;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/6/1 10:49 <br/>
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class QuartzJobDetailService {

    @Autowired
    private QuartzTriggerService triggerService;

    /**
     * 数据转换
     */
    private List<QuartzJobDetails> convertQuartzJobDetails(List<JobDetail> jobDetailList) {
        List<QuartzJobDetails> qrtzJobDetailsList = new ArrayList<>();
        String schedName = null;
        try {
            schedName = QuartzManager.getScheduler().getSchedulerName();
        } catch (Throwable e) {
            log.error("获取SchedulerName失败", e);
        }
        for (JobDetail jobDetail : jobDetailList) {
            QuartzJobDetails qrtzJobDetails = new QuartzJobDetails();
            qrtzJobDetails.setSchedName(schedName);
            qrtzJobDetails.setJobGroup(jobDetail.getKey().getGroup());
            qrtzJobDetails.setJobName(jobDetail.getKey().getName());
            qrtzJobDetails.setDurable(jobDetail.isDurable());
            qrtzJobDetails.setDescription(jobDetail.getDescription());
            qrtzJobDetails.setJobClassName(jobDetail.getJobClass().getName());
            qrtzJobDetails.setJobData(jobDetail.getJobDataMap());
            qrtzJobDetails.setRequestsRecovery(jobDetail.requestsRecovery());
            // @DisallowConcurrentExecution 对应 isNonconcurrent
            // @PersistJobDataAfterExecution 对应 isUpdateData
            qrtzJobDetails.setNonconcurrent(jobDetail.isConcurrentExectionDisallowed());
            qrtzJobDetails.setUpdateData(jobDetail.isPersistJobDataAfterExecution());

            AjaxMessage ajaxMessage = new AjaxMessage(true, null, null);
            List<QuartzTriggers> triggersList = triggerService.getTriggerByJob(new JobDetailKeyRes(jobDetail.getKey().getName(), jobDetail.getKey().getGroup()), ajaxMessage);
            qrtzJobDetails.setTriggersList(triggersList);

            qrtzJobDetailsList.add(qrtzJobDetails);
        }
        return qrtzJobDetailsList;
    }

    /**
     * 获取所有的JobKey
     */
    public List<JobKey> getAllJobKey(AjaxMessage<List<JobKey>> ajaxMessage) {
        List<JobKey> jobKeyList = QuartzManager.getAllJobKey();
        if (jobKeyList == null) {
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("获取所有的JobKey失败");
        }
        return jobKeyList;
    }

    /**
     * 获取所有的 JobDetail
     */
    public List<QuartzJobDetails> getAllJobDetail() {
        List<JobDetail> jobDetailList = QuartzManager.getAllJobDetail();
        return convertQuartzJobDetails(jobDetailList);
    }

    /**
     * 查询 JobDetail
     *
     * @return JobDetail集合
     */
    public List<QuartzJobDetails> findJobDetail(FindJobDetailRes findJobDetailRes) {
        List<JobDetail> jobDetailList = QuartzManager.findJobDetail(findJobDetailRes.getJobName(), findJobDetailRes.getJobGroup());
        return convertQuartzJobDetails(jobDetailList);
    }

    /**
     * 返回basePackage包下面所有的Job子类
     */
    public List<String> getAllJobClassName() {
        List<String> jobClassNameList = new ArrayList<>();
        jobClassNameList.addAll(QuartzManager.getAllJobClassName("org.clever.quartz.jobs"));
        jobClassNameList.addAll(QuartzManager.getAllJobClassName("org.quartz"));
        return jobClassNameList;
    }

    /**
     * 获取所有的JobGroupName
     */
    public List<String> getJobGroupNames(AjaxMessage<List<String>> ajaxMessage) {
        List<String> jobGroupNames = QuartzManager.getJobGroupNames();
        if (jobGroupNames == null) {
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("获取所有的JobGroupName失败");
        }
        return jobGroupNames;
    }

    /**
     * 保存一个JobDetail,只是保存一个JobDetail并不设置其 Trigger
     *
     * @return 成功返回true
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public boolean saveJobDetail(SaveJobDetailRes saveJobDetailRes, AjaxMessage ajaxMessage) {
        Scheduler scheduler = QuartzManager.getScheduler();
        Class aClass = QuartzManager.getJobClass(saveJobDetailRes.getJobClassName());
        if (aClass == null) {
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("JobClassName错误[" + saveJobDetailRes.getJobClassName() + "]");
            return false;
        }
        JobBuilder jobBuilder = JobBuilder.newJob(aClass);
        jobBuilder.withIdentity(saveJobDetailRes.getJobName(), saveJobDetailRes.getJobGroup());
        // 需要存储的job必须调用此方法
        jobBuilder.storeDurably();
        jobBuilder.withDescription(saveJobDetailRes.getDescription());
        jobBuilder.requestRecovery(saveJobDetailRes.getRequestsRecovery());
        if (saveJobDetailRes.getJobData() != null) {
            for (Map.Entry<String, String> entry : saveJobDetailRes.getJobData().entrySet()) {
                jobBuilder.usingJobData(entry.getKey(), entry.getValue());
            }
        }
        // @DisallowConcurrentExecution 对应 isNonconcurrent
        // @PersistJobDataAfterExecution 对应 isUpdateData
        JobDetail jobDetail = jobBuilder.build();
        try {
            scheduler.addJob(jobDetail, false);
        } catch (Throwable e) {
            log.error("保存JobDetail失败", e);
            throw ExceptionUtils.unchecked(e);
        }
        return true;
    }

    /**
     * 停止并且删除一个JobDetail
     *
     * @return 成功返回true
     */
    @Transactional
    public boolean deleteJobDetail(JobDetailKeyRes jobDetailKeyRes, AjaxMessage ajaxMessage) {
        Scheduler scheduler = QuartzManager.getScheduler();
        try {
            JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobDetailKeyRes.getJobName(), jobDetailKeyRes.getJobGroup()));
            if (jobDetail == null) {
                ajaxMessage.setSuccess(false);
                ajaxMessage.setFailMessage("删除任务失败,任务不存在");
                return false;
            }

            List<? extends Trigger> jobTriggers = QuartzManager.getTriggerByJob(jobDetailKeyRes.getJobName(), jobDetailKeyRes.getJobGroup());
            if (jobTriggers == null) {
                ajaxMessage.setSuccess(false);
                ajaxMessage.setFailMessage("删除JobDetail失败-获取JobDetail的所有Trigger失败");
                return false;
            }
            for (Trigger trigger : jobTriggers) {
                // 暂停触发器
                scheduler.pauseTrigger(trigger.getKey());
                // 移除触发器
                scheduler.unscheduleJob(trigger.getKey());
            }
            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(jobDetailKeyRes.getJobName(), jobDetailKeyRes.getJobGroup()));
        } catch (Throwable e) {
            log.error("删除JobDetail发生异常", e);
            throw ExceptionUtils.unchecked(e);
        }
        return true;
    }

    /**
     * 暂停一个JobDetail
     *
     * @return 成功返回true
     */
    @Transactional
    public boolean pauseJob(JobDetailKeyRes jobDetailKeyRes, AjaxMessage ajaxMessage) {
        Scheduler scheduler = QuartzManager.getScheduler();
        try {
            JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobDetailKeyRes.getJobName(), jobDetailKeyRes.getJobGroup()));
            if (jobDetail == null) {
                ajaxMessage.setSuccess(false);
                ajaxMessage.setFailMessage("暂停任务失败,任务不存在");
                return false;
            }
            scheduler.pauseJob(JobKey.jobKey(jobDetailKeyRes.getJobName(), jobDetailKeyRes.getJobGroup()));
        } catch (Throwable e) {
            log.error("暂停JobDetail异常", e);
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("暂停JobDetail失败");
            return false;
        }
        return true;
    }

    /**
     * 取消暂停一个JobDetail
     *
     * @return 成功返回true
     */
    @Transactional
    public boolean resumeJob(JobDetailKeyRes jobDetailKeyRes, AjaxMessage ajaxMessage) {
        Scheduler scheduler = QuartzManager.getScheduler();
        try {
            JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobDetailKeyRes.getJobName(), jobDetailKeyRes.getJobGroup()));
            if (jobDetail == null) {
                ajaxMessage.setSuccess(false);
                ajaxMessage.setFailMessage("继续运行任务失败,任务不存在");
                return false;
            }
            scheduler.resumeJob(JobKey.jobKey(jobDetailKeyRes.getJobName(), jobDetailKeyRes.getJobGroup()));
        } catch (Throwable e) {
            log.error("取消暂停JobDetail异常", e);
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("取消暂停JobDetail失败");
            return false;
        }
        return true;
    }

    /**
     * 立即运行JobDetail
     *
     * @return 成功返回true
     */
    @Transactional
    public boolean triggerJob(JobDetailKeyRes jobDetailKeyRes, AjaxMessage ajaxMessage) {
        Scheduler scheduler = QuartzManager.getScheduler();
        try {
            JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobDetailKeyRes.getJobName(), jobDetailKeyRes.getJobGroup()));
            if (jobDetail == null) {
                ajaxMessage.setSuccess(false);
                ajaxMessage.setFailMessage("立即执行任务失败,任务不存在");
                return false;
            }
            scheduler.triggerJob(JobKey.jobKey(jobDetailKeyRes.getJobName(), jobDetailKeyRes.getJobGroup()));
        } catch (Throwable e) {
            log.error("立即运行JobDetail异常", e);
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("立即运行JobDetail失败");
            return false;
        }
        return true;
    }

}
