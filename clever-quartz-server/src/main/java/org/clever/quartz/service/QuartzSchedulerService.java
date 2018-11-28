package org.clever.quartz.service;


import lombok.extern.slf4j.Slf4j;
import org.clever.common.exception.BusinessException;
import org.clever.quartz.dto.response.CurrentlyExecutingJobsRes;
import org.clever.quartz.entity.QrtzSchedulerState;
import org.clever.quartz.mapper.QrtzSchedulerStateMapper;
import org.clever.quartz.utils.ConvertUtils;
import org.clever.quartz.utils.QuartzManager;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：LiZW <br/>
 * 创建时间：2016-8-8 16:37 <br/>
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class QuartzSchedulerService {

    @Autowired
    private QrtzSchedulerStateMapper qrtzSchedulerStateMapper;

    public List<QrtzSchedulerState> allScheduler() {
        return qrtzSchedulerStateMapper.selectList(null);
    }

    /**
     * 暂停调度器
     *
     * @return 失败返回false
     */
    @Transactional
    public SchedulerMetaData standby() {
        boolean result = QuartzManager.standby();
        if (!result) {
            throw new BusinessException("暂停调度器失败");
        }
        return getMetaData();
    }

    /**
     * 继续运行调度器
     *
     * @return 失败返回false
     */
    @Transactional
    public SchedulerMetaData start() {
        boolean result = QuartzManager.start();
        if (!result) {
            throw new BusinessException("继续运行调度器失败");
        }
        return getMetaData();
    }

    /**
     * 暂停所有的触发器
     *
     * @return 失败返回false
     */
    @Transactional
    public SchedulerMetaData pauseAll() {
        Scheduler scheduler = QuartzManager.getScheduler();
        try {
            scheduler.pauseAll();
        } catch (Throwable e) {
            log.error("暂停所有的触发器异常", e);
            throw new BusinessException("继续运行调度器失败", e);
        }
        return getMetaData();
    }

    /**
     * 取消暂停所有的触发器
     *
     * @return 失败返回false
     */
    @Transactional
    public SchedulerMetaData resumeAll() {
        Scheduler scheduler = QuartzManager.getScheduler();
        try {
            scheduler.resumeAll();
        } catch (Throwable e) {
            throw new BusinessException("取消暂停所有的触发器异常", e);
        }
        return getMetaData();
    }

    /**
     * 返回正在运行的Job<br/>
     * <b>注意:此方法不支持集群</b>
     *
     * @return 失败返回null
     */
    public List<CurrentlyExecutingJobsRes> getRunningJobs() {
        Scheduler scheduler = QuartzManager.getScheduler();
        List<JobExecutionContext> list;
        String schedName;
        try {
            schedName = QuartzManager.getScheduler().getSchedulerName();
            list = scheduler.getCurrentlyExecutingJobs();
        } catch (Throwable e) {
            log.error("获取当前所有执行的JobExecutionContext异常", e);
            throw new BusinessException("获取当前所有执行的JobExecutionContext异常", e);
        }
        return ConvertUtils.convert(schedName, list);
    }

    /**
     * 中断Job<br/>
     * <b>注意:被中断的Job,必须实现接口 InterruptableJob</b>
     *
     * @return 失败返回false
     */
    @Transactional
    public boolean interrupt(JobKey jobKey) {
        Scheduler scheduler = QuartzManager.getScheduler();
        try {
            scheduler.interrupt(jobKey);
        } catch (Throwable e) {
            log.error("中断Job异常", e);
            throw new BusinessException("中断Job异常", e);
        }
        return true;
    }

    /**
     * 获取Scheduler信息
     *
     * @return 失败返回null
     */
    public SchedulerMetaData getMetaData() {
        Scheduler scheduler = QuartzManager.getScheduler();
        SchedulerMetaData schedulerMetaData;
        try {
            schedulerMetaData = scheduler.getMetaData();
        } catch (Throwable e) {
            log.error("获取Scheduler信息异常", e);
            throw new BusinessException("获取Scheduler信息异常", e);
        }
        return schedulerMetaData;
    }

    /**
     * 获取SchedulerContext
     *
     * @return 失败返回null
     */
    public Map<String, Object> getContext() {
        Scheduler scheduler = QuartzManager.getScheduler();
        SchedulerContext schedulerContext = null;
        try {
            schedulerContext = scheduler.getContext();
        } catch (Throwable e) {
            log.error("获取SchedulerContext异常", e);
            throw new BusinessException("获取SchedulerContext异常", e);
        }
        Map<String, Object> result = null;
        if (schedulerContext != null) {
            result = new HashMap<>();
            for (Map.Entry<String, Object> entry : schedulerContext.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof Serializable) {
                    result.put(entry.getKey(), value);
                } else {
                    result.put(entry.getKey(), value.getClass().getName());
                }
            }
        }
        return result;
    }
}
