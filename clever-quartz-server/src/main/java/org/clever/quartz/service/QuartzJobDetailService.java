package org.clever.quartz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.model.response.AjaxMessage;
import org.clever.common.utils.exception.ExceptionUtils;
import org.clever.quartz.dto.request.FindJobDetailReq;
import org.clever.quartz.dto.request.JobDetailKeyReq;
import org.clever.quartz.dto.request.SaveJobDetailReq;
import org.clever.quartz.dto.response.JobDetailInfoRes;
import org.clever.quartz.dto.response.JobDetailsRes;
import org.clever.quartz.dto.response.JobKeyRes;
import org.clever.quartz.entity.QrtzTriggers;
import org.clever.quartz.mapper.QrtzJobDetailsMapper;
import org.clever.quartz.mapper.QrtzTriggersMapper;
import org.clever.quartz.utils.ConvertUtils;
import org.clever.quartz.utils.QuartzManager;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private QrtzJobDetailsMapper qrtzJobDetailsMapper;
    @Autowired
    private QrtzTriggersMapper qrtzTriggersMapper;

    /**
     * 返回basePackage包下面所有的Job子类
     */
    public List<String> getAllJobClassName() {
        return QuartzManager.getAllJobClassName("org.clever.quartz.jobs");
    }

    /**
     * 获取所有的JobGroupName
     */
    public List<String> getJobGroupNames(AjaxMessage<List<String>> ajaxMessage) {
        Scheduler scheduler = QuartzManager.getScheduler();
        List<String> jobGroupNames = null;
        try {
            jobGroupNames = scheduler.getJobGroupNames();
        } catch (Throwable e) {
            log.error("### 获取所有的JobGroupName失败", e);
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("获取所有的JobGroupName失败");
        }
        return jobGroupNames;
    }

    /**
     * 根据JobGroup查询JobKey
     */
    public List<JobKeyRes> getJobKeyByGroup(String jobGroup, AjaxMessage ajaxMessage) {
        Scheduler scheduler = QuartzManager.getScheduler();
        List<JobKeyRes> jobKeyResList = null;
        try {
            jobKeyResList = qrtzJobDetailsMapper.getJobKeyByGroup(scheduler.getSchedulerName(), jobGroup);
        } catch (Throwable e) {
            log.error("### 根据JobGroup查询JobKey失败", e);
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("根据JobGroup查询JobKey失败");
        }
        return jobKeyResList;
    }

    /**
     * 查询 JobDetail
     *
     * @return JobDetail集合
     */
    public List<JobDetailsRes> findJobDetail(FindJobDetailReq req, AjaxMessage ajaxMessage) {
        Scheduler scheduler = QuartzManager.getScheduler();
        List<JobDetailsRes> list = null;
        try {
            Page<JobDetailsRes> page = new Page<>(req.getPageNo(), req.getPageSize());
            list = qrtzJobDetailsMapper.find(scheduler.getSchedulerName(), req.getJobName(), req.getJobGroup(), req.getJobClassName(), page);
        } catch (Throwable e) {
            log.error("### 查询JobDetail失败", e);
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("查询JobDetail失败");
        }
        return list;
    }

    public JobDetailInfoRes getJobDetails(String jobGroup, String jobName, AjaxMessage ajaxMessage) {
        Scheduler scheduler = QuartzManager.getScheduler();
        JobDetailInfoRes jobDetailInfoRes = null;
        try {
            JobDetail jobDetail = scheduler.getJobDetail(new JobKey(jobName, jobGroup));
            jobDetailInfoRes = ConvertUtils.convert(scheduler.getSchedulerName(), jobDetail);
        } catch (Throwable e) {
            log.error("### 获取Job信息失败", e);
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("获取Job信息失败");
        }
        return jobDetailInfoRes;
    }

    /**
     * 保存一个JobDetail,只是保存一个JobDetail并不设置其 Trigger
     *
     * @return 成功返回true
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public boolean saveJobDetail(SaveJobDetailReq saveJobDetailReq, AjaxMessage ajaxMessage) {
        Scheduler scheduler = QuartzManager.getScheduler();
        Class aClass = QuartzManager.getJobClass(saveJobDetailReq.getJobClassName());
        if (aClass == null) {
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("JobClassName错误[" + saveJobDetailReq.getJobClassName() + "]");
            return false;
        }
        JobBuilder jobBuilder = JobBuilder.newJob(aClass);
        jobBuilder.withIdentity(saveJobDetailReq.getJobName(), saveJobDetailReq.getJobGroup());
        // 需要存储的job必须调用此方法
        jobBuilder.storeDurably();
        jobBuilder.withDescription(saveJobDetailReq.getDescription());
        jobBuilder.requestRecovery(saveJobDetailReq.getRequestsRecovery());
        if (saveJobDetailReq.getJobData() != null) {
            for (Map.Entry<String, String> entry : saveJobDetailReq.getJobData().entrySet()) {
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
    public boolean deleteJobDetail(JobDetailKeyReq jobDetailKeyReq, AjaxMessage ajaxMessage) {
        Scheduler scheduler = QuartzManager.getScheduler();
        try {
            JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobDetailKeyReq.getJobName(), jobDetailKeyReq.getJobGroup()));
            if (jobDetail == null) {
                ajaxMessage.setSuccess(false);
                ajaxMessage.setFailMessage("删除任务失败,任务不存在");
                return false;
            }

            List<QrtzTriggers> jobTriggers = qrtzTriggersMapper.getByJobKey(scheduler.getSchedulerName(), jobDetailKeyReq.getJobName(), jobDetailKeyReq.getJobGroup());
            if (jobTriggers == null) {
                ajaxMessage.setSuccess(false);
                ajaxMessage.setFailMessage("删除JobDetail失败-获取JobDetail的所有Trigger失败");
                return false;
            }
            for (QrtzTriggers trigger : jobTriggers) {
                // 暂停触发器
                scheduler.pauseTrigger(new TriggerKey(trigger.getJobName(), trigger.getJobGroup()));
                // 移除触发器
                scheduler.unscheduleJob(new TriggerKey(trigger.getJobName(), trigger.getJobGroup()));
            }
            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(jobDetailKeyReq.getJobName(), jobDetailKeyReq.getJobGroup()));
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
    public boolean pauseJob(JobDetailKeyReq jobDetailKeyReq, AjaxMessage ajaxMessage) {
        Scheduler scheduler = QuartzManager.getScheduler();
        try {
            JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobDetailKeyReq.getJobName(), jobDetailKeyReq.getJobGroup()));
            if (jobDetail == null) {
                ajaxMessage.setSuccess(false);
                ajaxMessage.setFailMessage("暂停任务失败,任务不存在");
                return false;
            }
            scheduler.pauseJob(JobKey.jobKey(jobDetailKeyReq.getJobName(), jobDetailKeyReq.getJobGroup()));
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
    public boolean resumeJob(JobDetailKeyReq jobDetailKeyReq, AjaxMessage ajaxMessage) {
        Scheduler scheduler = QuartzManager.getScheduler();
        try {
            JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobDetailKeyReq.getJobName(), jobDetailKeyReq.getJobGroup()));
            if (jobDetail == null) {
                ajaxMessage.setSuccess(false);
                ajaxMessage.setFailMessage("继续运行任务失败,任务不存在");
                return false;
            }
            scheduler.resumeJob(JobKey.jobKey(jobDetailKeyReq.getJobName(), jobDetailKeyReq.getJobGroup()));
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
    public boolean triggerJob(JobDetailKeyReq jobDetailKeyReq, AjaxMessage ajaxMessage) {
        Scheduler scheduler = QuartzManager.getScheduler();
        try {
            JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobDetailKeyReq.getJobName(), jobDetailKeyReq.getJobGroup()));
            if (jobDetail == null) {
                ajaxMessage.setSuccess(false);
                ajaxMessage.setFailMessage("立即执行任务失败,任务不存在");
                return false;
            }
            scheduler.triggerJob(JobKey.jobKey(jobDetailKeyReq.getJobName(), jobDetailKeyReq.getJobGroup()));
        } catch (Throwable e) {
            log.error("立即运行JobDetail异常", e);
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("立即运行JobDetail失败");
            return false;
        }
        return true;
    }

}
