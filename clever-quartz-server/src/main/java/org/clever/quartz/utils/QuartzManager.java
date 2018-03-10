package org.clever.quartz.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.exception.ExceptionUtils;
import org.clever.common.utils.spring.SpringContextHolder;
import org.clever.quartz.mapper.QrtzJobDetailsMapper;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.reflections.Reflections;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Quartz管理类
 * <p>
 * 作者：LiZW <br/>
 * 创建时间：2016-7-29 11:39 <br/>
 */
@Slf4j
public class QuartzManager {

    private final static Scheduler SCHEDULER;

    static {
        try {
            SCHEDULER = SpringContextHolder.getBean(Scheduler.class);
            if (SCHEDULER != null) {
                log.info("### SchedulerFactoryBean注入成功");
            } else {
                RuntimeException exception = new RuntimeException("### SchedulerFactoryBean注入失败");
                log.error("### SchedulerFactoryBean注入失败", exception);
                throw exception;
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw ExceptionUtils.unchecked(e);
        }
    }

    /**
     * 返回basePackage包下面所有的Job子类
     *
     * @param basePackage 扫描的基础包
     * @return 所有的Job子类集合
     */
    @SuppressWarnings({"deprecation"})
    public static List<String> getAllJobClassName(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<? extends Job>> jobClassSet = new HashSet<>();
        jobClassSet.addAll(reflections.getSubTypesOf(Job.class));
        jobClassSet.addAll(reflections.getSubTypesOf(InterruptableJob.class));
        jobClassSet.addAll(reflections.getSubTypesOf(StatefulJob.class));
        jobClassSet.addAll(reflections.getSubTypesOf(QuartzJobBean.class));
        List<String> JobClassNameList = jobClassSet.stream().map(Class::getName).collect(Collectors.toList());
        JobClassNameList.remove("org.quartz.Job");
        JobClassNameList.remove("org.quartz.InterruptableJob");
        JobClassNameList.remove("org.quartz.StatefulJob");
        JobClassNameList.remove("org.springframework.scheduling.quartz.QuartzJobBean");
        return JobClassNameList;
    }

    public static Scheduler getScheduler() {
        if (SCHEDULER == null) {
            RuntimeException exception = new RuntimeException("### SchedulerFactoryBean注入失败");
            log.error("### SchedulerFactoryBean注入失败", exception);
            throw exception;
        }
        return SCHEDULER;
    }

    /**
     * 反射获取Job类型
     *
     * @param jobClassName 类全名
     * @return 失败返回null
     */
    public static Class getJobClass(String jobClassName) {
        try {
            Class aClass = Class.forName(jobClassName);
            if (Job.class.isAssignableFrom(aClass)) {
                return aClass;
            } else {
                log.warn("### 类型[" + jobClassName + "]不是org.quartz.Job的子类");
            }
        } catch (Throwable e) {
            log.error("### 获取Job类型失败[" + jobClassName + "]", e);
        }
        return null;
    }

    /**
     * 启动 Scheduler
     *
     * @return 操作成功返回true
     */
    public static boolean start() {
        try {
            if (!SCHEDULER.isStarted() || SCHEDULER.isInStandbyMode()) {
                SCHEDULER.start();
            }
        } catch (Throwable e) {
            log.error("### Scheduler启动失败", e);
            return false;
        }
        log.info("### 成功启动 Scheduler");
        return true;
    }

    /**
     * 关闭 Scheduler
     *
     * @return 操作成功返回true
     */
    public static boolean shutdown() {
        try {
            if (!SCHEDULER.isShutdown()) {
                SCHEDULER.shutdown();
            }
        } catch (Throwable e) {
            log.error("### Scheduler关闭失败", e);
            return false;
        }
        log.info("### 成功关闭 Scheduler");
        return true;
    }

    /**
     * 暂停 Scheduler<br/>
     * 调用 {@link #start()} 继续运行调度器(Scheduler)
     *
     * @return 操作成功返回true
     * @see #start()
     */
    public static boolean standby() {
        try {
            if (!SCHEDULER.isInStandbyMode()) {
                SCHEDULER.standby();
            }
        } catch (Throwable e) {
            log.error("### Scheduler暂停失败", e);
            return false;
        }
        log.info("### 成功暂停 Scheduler");
        return true;
    }

    /**
     * 验证cron表达式,失败返回null
     *
     * @param cron cron表达式
     * @param num  获取cron表达式表示时间数量
     * @return 失败返回null
     */
    public static List<Date> validatorCron(String cron, int num) {
        List<Date> dateList = new ArrayList<>();
        CronExpression exp;
        try {
            exp = new CronExpression(cron);
        } catch (Throwable e) {
            log.error("cron表达式错误[" + cron + "]", e);
            return null;
        }
        Date date = new Date();
        // 循环得到接下来n此的触发时间点，供验证
        for (int i = 0; i < num; i++) {
            date = exp.getNextValidTimeAfter(date);
            dateList.add(date);
        }
        return dateList;
    }

    // --------------------------------------------------------------------------------------------------


    /**
     * 获取所有的TriggerGroupName
     *
     * @return 失败返回null
     */
    public static List<String> getTriggerGroupNames() {
        List<String> triggerGroupNames = null;
        try {
            triggerGroupNames = SCHEDULER.getTriggerGroupNames();
        } catch (Throwable e) {
            log.error("### 获取所有的TriggerGroupName失败", e);
        }
        return triggerGroupNames;
    }

    /**
     * 获取所有的CalendarName
     *
     * @return 失败返回null
     */
    public static List<String> getCalendarNames() {
        List<String> calendarName = null;
        try {
            calendarName = SCHEDULER.getCalendarNames();
        } catch (Throwable e) {
            log.error("### 获取所有的CalendarName失败", e);
        }
        return calendarName;
    }

//    /**
//     * 获取所有的JobKey
//     *
//     * @return 失败返回null
//     */
//    public static List<JobKey> getAllJobKey() {
//        List<JobKey> jobKeyList;
//        try {
//            jobKeyList = new ArrayList<>(SCHEDULER.getJobKeys(GroupMatcher.anyGroup()));
//        } catch (Throwable e) {
//            log.error("### 获取所有的JobKey失败", e);
//            return null;
//        }
//        return jobKeyList;
//    }


//    /**
//     * 获取所有的 JobDetail
//     *
//     * @return JobDetail集合
//     */
//    public static List<JobDetail> getAllJobDetail() {
//        QrtzJobDetailsMapper qrtzJobDetailsMapper = SpringContextHolder.getBean(QrtzJobDetailsMapper.class);
////        qrtzJobDetailsMapper.find(SCHEDULER.getSchedulerName(), null, null);
//
//
//        List<JobDetail> jobDetailList = new ArrayList<>();
//
////
//
//
//        List<JobKey> jobKeyList = getAllJobKey();
//        if (jobKeyList == null) {
//            return null;
//        }
//        for (JobKey jobKey : jobKeyList) {
//            JobDetail jobDetail = null;
//            try {
//                jobDetail = SCHEDULER.getJobDetail(jobKey);
//            } catch (Throwable e) {
//                log.error("### 获取JobDetail失败[jobName=" + jobKey.getName() + ",jobGroup=" + jobKey.getGroup() + "]", e);
//            }
//            if (jobDetail != null) {
//                jobDetailList.add(jobDetail);
//            }
//        }
//        return jobDetailList;
//    }

//    /**
//     * 查询 JobDetail
//     *
//     * @param jobName  任务名
//     * @param jobGroup 任务组名
//     * @return JobDetail集合
//     */
//    public static List<JobDetail> findJobDetail(String jobName, String jobGroup) {
//        List<JobDetail> jobDetailList = new ArrayList<>();
//        List<JobKey> jobKeyList = getAllJobKey();
//        if (jobKeyList == null) {
//            return null;
//        }
//        for (JobKey jobKey : jobKeyList) {
//            if ((StringUtils.isBlank(jobName) || jobKey.getName().contains(jobName)) && (StringUtils.isBlank(jobGroup) || jobKey.getGroup().contains(jobGroup))) {
//                JobDetail jobDetail = null;
//                try {
//                    jobDetail = SCHEDULER.getJobDetail(jobKey);
//                } catch (Throwable e) {
//                    log.error("### 获取JobDetail失败[jobName=" + jobKey.getName() + ",jobGroup=" + jobKey.getGroup() + "]", e);
//                }
//                if (jobDetail != null) {
//                    jobDetailList.add(jobDetail);
//                }
//            }
//        }
//        return jobDetailList;
//    }

    /**
     * 获取一个Job的所有 Trigger
     *
     * @param jobName  job名称
     * @param jobGroup job组名称
     * @return 失败返回null
     */
    public static List<? extends Trigger> getTriggerByJob(String jobName, String jobGroup) {
        List<? extends Trigger> jobTriggers = null;
        try {
            jobTriggers = SCHEDULER.getTriggersOfJob(JobKey.jobKey(jobName, jobGroup));
        } catch (Throwable e) {
            log.error("### 获取一个Job的所有Trigger失败 [jobName=" + jobName + ", jobGroup=" + jobGroup + "]", e);
        }
        return jobTriggers;
    }
}
