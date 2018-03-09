package org.clever.quartz.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.model.response.AjaxMessage;
import org.clever.common.utils.DateTimeUtils;
import org.clever.common.utils.exception.ExceptionUtils;
import org.clever.quartz.dto.request.AddCronTriggerForJobReq;
import org.clever.quartz.dto.request.AddSimpleTriggerForJobReq;
import org.clever.quartz.dto.request.JobDetailKeyReq;
import org.clever.quartz.dto.request.TriggerKeyReq;
import org.clever.quartz.entity.QrtzTriggers;
import org.clever.quartz.mapper.QrtzTriggersMapper;
import org.clever.quartz.model.QuartzTriggers;
import org.clever.quartz.utils.QuartzManager;
import org.quartz.*;
import org.quartz.impl.triggers.CalendarIntervalTriggerImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.DailyTimeIntervalTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 作者：LiZW <br/>
 * 创建时间：2016-7-29 16:43 <br/>
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class QuartzTriggerService {

    @Autowired
    private QrtzTriggersMapper qrtzTriggersMapper;

    /**
     * 使用Trigger的基础属性创建一个TriggerBuilder
     *
     * @param jobName      job名称,必填
     * @param jobGroup     job组名称,必填
     * @param triggerName  trigger组名称,必填
     * @param triggerGroup trigger组名称,必填
     * @param description  trigger描述,可为null
     * @param startTime    开始触发时间,必填
     * @param endTime      结束触发时间,可为null
     * @param priority     优先级,可为null
     * @param jobDataMap   Trigger数据,可为null
     * @return 返回一个 TriggerBuilder
     */
    public static TriggerBuilder<Trigger> newTriggerBuilder(
            String jobName,
            String jobGroup,
            String triggerName,
            String triggerGroup,
            String description,
            Date startTime,
            Date endTime,
            Integer priority,
            Map<String, String> jobDataMap) {
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().forJob(jobName, jobGroup);
        triggerBuilder.withIdentity(triggerName, triggerGroup);
        triggerBuilder.withDescription(description);
        triggerBuilder.startAt(startTime);
        if (endTime != null) {
            triggerBuilder.endAt(endTime);
        }
        if (priority != null) {
            triggerBuilder.withPriority(priority);
        }
        if (jobDataMap != null && jobDataMap.size() > 0) {
            for (Map.Entry<String, String> entry : jobDataMap.entrySet()) {
                triggerBuilder.usingJobData(entry.getKey(), entry.getValue());
            }
        }
        return triggerBuilder;
    }

    /**
     * 创建一个CronScheduleBuilder
     *
     * @param cron               cron表达式,必填
     * @param misfireInstruction Quartz的Misfire处理规则(CronTrigger),可为null
     * @return 返回一个 CronScheduleBuilder
     */
    public static CronScheduleBuilder newCronScheduleBuilder(String cron, Integer misfireInstruction) {
        CronScheduleBuilder cronScheduleBuilder;
        try {
            cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        } catch (Throwable e) {
            log.error("创建CronScheduleBuilder异常", e);
            return null;
        }
        if (misfireInstruction != null) {
            switch (misfireInstruction) {
                case Trigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY:
                    cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
                    break;
                case CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING:
                    cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
                    break;
                case CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW:
                    cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
                    break;
            }
        }
        return cronScheduleBuilder;
    }

    /**
     * 验证cron表达式,失败返回null
     *
     * @param cron cron表达式
     * @param num  获取cron表达式表示时间数量
     * @return 失败返回null
     */
    public List<String> validatorCron(String cron, int num, AjaxMessage ajaxMessage) {
        List<Date> dateList = QuartzManager.validatorCron(cron, num);
        List<String> daStrList = new ArrayList<>();
        if (dateList == null) {
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("cron表达式验证失败");
        } else {
            for (Date date : dateList) {
                daStrList.add(DateTimeUtils.formatToString(date, "yyyy-MM-dd HH:mm:ss"));
            }
        }
        return daStrList;
    }

    /**
     * 创建一个SimpleScheduleBuilder
     *
     * @param interval           触发的时间间隔(毫秒),必填
     * @param repeatCount        触发的次数,可为null,次数不限取值:-1(SimpleTrigger.REPEAT_INDEFINITELY)
     * @param misfireInstruction Quartz的Misfire处理规则(SimpleTrigger),可为null
     * @return 返回一个 SimpleScheduleBuilder
     */
    private SimpleScheduleBuilder newSimpleScheduleBuilder(long interval, Integer repeatCount, Integer misfireInstruction) {
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
        simpleScheduleBuilder.withIntervalInMilliseconds(interval);
        if (repeatCount != null && repeatCount >= 1) {
            simpleScheduleBuilder.withRepeatCount(repeatCount);
        }
        if (misfireInstruction != null) {
            switch (misfireInstruction) {
                case SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW:
                    simpleScheduleBuilder.withMisfireHandlingInstructionFireNow();
                    break;
                case Trigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY:
                    simpleScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
                    break;
                case SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT:
                    simpleScheduleBuilder.withMisfireHandlingInstructionNextWithExistingCount();
                    break;
                case SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT:
                    simpleScheduleBuilder.withMisfireHandlingInstructionNextWithRemainingCount();
                    break;
                case SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT:
                    simpleScheduleBuilder.withMisfireHandlingInstructionNowWithExistingCount();
                    break;
                case SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT:
                    simpleScheduleBuilder.withMisfireHandlingInstructionNowWithRemainingCount();
                    break;
            }
        }
        return simpleScheduleBuilder;
    }

    /**
     * 给JobDetail增加一个SimpleTrigger
     * <pre>
     * Quartz的Misfire处理规则(SimpleTrigger)
     * 1.withMisfireHandlingInstructionFireNow
     *      -以当前时间为触发频率立即触发执行
     *      -执行至FinalTime的剩余周期次数
     *      -以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到
     *      -调整后的FinalTime会略大于根据startTime计算的到的FinalTime值
     * 2.withMisfireHandlingInstructionIgnoreMisfires
     *      -以错过的第一个频率时间立刻开始执行
     *      -重做错过的所有频率周期
     *      -当下一次触发频率发生时间大于当前时间以后，按照Interval的依次执行剩下的频率
     *      -共执行RepeatCount+1次
     * 3.withMisfireHandlingInstructionNextWithExistingCount
     *      -不触发立即执行
     *      -等待下次触发频率周期时刻，执行至FinalTime的剩余周期次数
     *      -以startTime为基准计算周期频率，并得到FinalTime
     *      -即使中间出现pause，resume以后保持FinalTime时间不变
     * 4.withMisfireHandlingInstructionNextWithRemainingCount
     *      -不触发立即执行
     *      -等待下次触发频率周期时刻，执行至FinalTime的剩余周期次数
     *      -以startTime为基准计算周期频率，并得到FinalTime
     *      -即使中间出现pause，resume以后保持FinalTime时间不变
     * 5.withMisfireHandlingInstructionNowWithRemainingCount
     *      -以当前时间为触发频率立即触发执行
     *      -执行至FinalTIme的剩余周期次数
     *      -以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到
     *      -调整后的FinalTime会略大于根据startTime计算的到的FinalTime值
     * 6.misfireInstructionRescheduleNowWithRemainingRepeatCount
     *      -此指令导致trigger忘记原始设置的startTime和repeat-count
     *      -触发器的repeat-count将被设置为剩余的次数
     *      -这样会导致后面无法获得原始设定的startTime和repeat-count值
     * </pre>
     *
     * @return 成功返回true
     */
    @Transactional
    public boolean addSimpleTriggerForJob(AddSimpleTriggerForJobReq addSimpleTriggerForJobReq, AjaxMessage ajaxMessage) {
        TriggerBuilder<Trigger> triggerBuilder = newTriggerBuilder(
                addSimpleTriggerForJobReq.getJobName(),
                addSimpleTriggerForJobReq.getJobGroup(),
                addSimpleTriggerForJobReq.getTriggerName(),
                addSimpleTriggerForJobReq.getTriggerGroup(),
                addSimpleTriggerForJobReq.getDescription(),
                addSimpleTriggerForJobReq.getStartTime(),
                addSimpleTriggerForJobReq.getEndTime(),
                addSimpleTriggerForJobReq.getPriority(),
                addSimpleTriggerForJobReq.getJobData());
        SimpleScheduleBuilder simpleScheduleBuilder = newSimpleScheduleBuilder(
                addSimpleTriggerForJobReq.getInterval(),
                addSimpleTriggerForJobReq.getRepeatCount(),
                addSimpleTriggerForJobReq.getMisfireInstruction());
        triggerBuilder.withSchedule(simpleScheduleBuilder);
        Trigger trigger = triggerBuilder.build();
        Scheduler scheduler = QuartzManager.getScheduler();
        try {
            scheduler.scheduleJob(trigger);
        } catch (Throwable e) {
            log.error("给JobDetail增加一个SimpleTrigger异常", e);
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("给JobDetail增加一个SimpleTrigger失败");
            return false;
        }
        return true;
    }

    /**
     * 给JobDetail增加一个SimpleTrigger
     * <pre>
     * 1.withMisfireHandlingInstructionDoNothing
     *      -不触发立即执行
     *      -等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
     * 2.withMisfireHandlingInstructionIgnoreMisfires
     *      -以错过的第一个频率时间立刻开始执行
     *      -重做错过的所有频率周期后
     *      -当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行
     * 3.withMisfireHandlingInstructionFireAndProceed
     *      -以当前时间为触发频率立刻触发一次执行
     *      -然后按照Cron频率依次执行
     * </pre>
     *
     * @return 成功返回true
     */
    @Transactional
    public boolean addCronTriggerForJob(AddCronTriggerForJobReq addCronTriggerForJobReq, AjaxMessage ajaxMessage) {
        TriggerBuilder<Trigger> triggerBuilder = newTriggerBuilder(
                addCronTriggerForJobReq.getJobName(),
                addCronTriggerForJobReq.getJobGroup(),
                addCronTriggerForJobReq.getTriggerName(),
                addCronTriggerForJobReq.getTriggerGroup(),
                addCronTriggerForJobReq.getDescription(),
                addCronTriggerForJobReq.getStartTime(),
                addCronTriggerForJobReq.getEndTime(),
                addCronTriggerForJobReq.getPriority(),
                addCronTriggerForJobReq.getJobData());
        CronScheduleBuilder cronScheduleBuilder = newCronScheduleBuilder(addCronTriggerForJobReq.getCron(), addCronTriggerForJobReq.getMisfireInstruction());
        triggerBuilder.withSchedule(cronScheduleBuilder);
        Trigger trigger = triggerBuilder.build();
        Scheduler scheduler = QuartzManager.getScheduler();
        try {
            scheduler.scheduleJob(trigger);
        } catch (Throwable e) {
            log.error("给JobDetail增加一个CronTrigger异常", e);
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("给JobDetail增加一个CronTrigger失败");
            return false;
        }
        return true;
    }

    /**
     * 获取一个Job的所有 Trigger
     *
     * @return 失败返回null
     */
    public List<QuartzTriggers> getTriggerByJob(JobDetailKeyReq jobDetailKeyReq, AjaxMessage ajaxMessage) {
        List<QuartzTriggers> qrtzTriggersList = new ArrayList<>();
        List<? extends Trigger> triggerList = QuartzManager.getTriggerByJob(jobDetailKeyReq.getJobName(), jobDetailKeyReq.getJobGroup());
        if (triggerList == null) {
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("获取JobDetail的所有Trigger失败");
            return null;
        }
        Scheduler scheduler = QuartzManager.getScheduler();
        String schedName = null;
        try {
            schedName = scheduler.getSchedulerName();
        } catch (Throwable e) {
            log.error("获取SchedulerName失败", e);
        }
        for (Trigger trigger : triggerList) {
            QuartzTriggers qrtzTriggers = new QuartzTriggers();
            qrtzTriggers.setSchedName(schedName);
            qrtzTriggers.setTriggerName(trigger.getKey().getName());
            qrtzTriggers.setTriggerGroup(trigger.getKey().getGroup());
            qrtzTriggers.setJobName(trigger.getJobKey().getName());
            qrtzTriggers.setJobGroup(trigger.getJobKey().getGroup());
            qrtzTriggers.setDescription(trigger.getDescription());
            qrtzTriggers.setNextFireTime(trigger.getNextFireTime());
            qrtzTriggers.setPrevFireTime(trigger.getPreviousFireTime());
            qrtzTriggers.setPriority(trigger.getPriority());
            try {
                QrtzTriggers qrtzTriggersTmp = qrtzTriggersMapper.getQrtzTriggers(schedName, qrtzTriggers.getTriggerGroup(), qrtzTriggers.getTriggerName());
                if (qrtzTriggersTmp == null) {
                    qrtzTriggers.setTriggerState(scheduler.getTriggerState(trigger.getKey()).name());
                } else {
                    qrtzTriggers.setTriggerState(qrtzTriggersTmp.getTriggerState());
                }
            } catch (Throwable e) {
                log.error("获取Trigger状态失败", e);
            }
            qrtzTriggers.setTriggerType(trigger.getClass().getName());
            qrtzTriggers.setStartTime(trigger.getStartTime());
            qrtzTriggers.setEndTime(trigger.getEndTime());
            qrtzTriggers.setCalendarName(trigger.getCalendarName());
            qrtzTriggers.setMisfireInstr(trigger.getMisfireInstruction());
            qrtzTriggers.setJobData(trigger.getJobDataMap());
            if (trigger instanceof SimpleTriggerImpl) {
                SimpleTriggerImpl simpleTrigger = (SimpleTriggerImpl) trigger;
                qrtzTriggers.setRepeatCount(simpleTrigger.getRepeatCount());
                qrtzTriggers.setRepeatInterval(simpleTrigger.getRepeatInterval());
                qrtzTriggers.setTimesTriggered(simpleTrigger.getTimesTriggered());
            }
            if (trigger instanceof CronTriggerImpl) {
                CronTriggerImpl cronTrigger = (CronTriggerImpl) trigger;
                qrtzTriggers.setCronEx(cronTrigger.getCronExpression());
                qrtzTriggers.setTimeZoneId(cronTrigger.getTimeZone() == null ? null : cronTrigger.getTimeZone().getID());
            }
            //noinspection StatementWithEmptyBody
            if (trigger instanceof CalendarIntervalTriggerImpl) {
                // TODO CalendarIntervalTriggerImpl

            }
            //noinspection StatementWithEmptyBody
            if (trigger instanceof DailyTimeIntervalTriggerImpl) {
                // TODO CalendarIntervalTriggerImpl
            }
            qrtzTriggersList.add(qrtzTriggers);
        }
        return qrtzTriggersList;
    }

    /**
     * 获取所有的TriggerGroupName
     *
     * @return 失败返回null
     */
    public List<String> getTriggerGroupNames(AjaxMessage ajaxMessage) {
        List<String> triggerGroupNames = QuartzManager.getTriggerGroupNames();
        if (triggerGroupNames == null) {
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("获取所有的TriggerGroupName失败");
        }
        return triggerGroupNames;
    }

    /**
     * 删除一个JobDetail的所有Trigger
     *
     * @return 成功返回true
     */
    @Transactional
    public boolean deleteTriggerByJob(JobDetailKeyReq jobDetailKeyReq, AjaxMessage ajaxMessage) {
        Scheduler scheduler = QuartzManager.getScheduler();
        List<? extends Trigger> jobTriggers = QuartzManager.getTriggerByJob(jobDetailKeyReq.getJobName(), jobDetailKeyReq.getJobGroup());
        if (jobTriggers == null) {
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("删除一个JobDetail的所有Trigger失败-获取JobDetail的所有Trigger失败");
            return false;
        }
        try {
            for (Trigger trigger : jobTriggers) {
                // 暂停触发器
                scheduler.pauseTrigger(trigger.getKey());
                // 移除触发器
                scheduler.unscheduleJob(trigger.getKey());
            }
        } catch (Throwable e) {
            log.error("删除一个JobDetail的所有Trigger异常", e);
            throw ExceptionUtils.unchecked(e);
        }
        return true;
    }

    /**
     * 暂停而且删除Trigger
     *
     * @return 成功返回true
     */
    @Transactional
    public boolean deleteTrigger(TriggerKeyReq triggerKeyReq, AjaxMessage ajaxMessage) {
        Scheduler scheduler = QuartzManager.getScheduler();
        try {
            // 暂停触发器
            scheduler.pauseTrigger(TriggerKey.triggerKey(triggerKeyReq.getTriggerName(), triggerKeyReq.getTriggerGroup()));
            // 移除触发器
            scheduler.unscheduleJob(TriggerKey.triggerKey(triggerKeyReq.getTriggerName(), triggerKeyReq.getTriggerGroup()));
        } catch (Throwable e) {
            log.error("删除一个Trigger异常", e);
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("删除一个Trigger失败");
            return false;
        }
        return true;
    }

    /**
     * 暂停一个 Trigger
     *
     * @return 成功返回true
     */
    @Transactional
    public boolean pauseTrigger(TriggerKeyReq triggerKeyReq, AjaxMessage ajaxMessage) {
        Scheduler scheduler = QuartzManager.getScheduler();
        try {
            // 暂停触发器
            scheduler.pauseTrigger(TriggerKey.triggerKey(triggerKeyReq.getTriggerName(), triggerKeyReq.getTriggerGroup()));
        } catch (Throwable e) {
            log.error("暂停一个Trigger异常", e);
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("暂停一个Trigger失败");
            return false;
        }
        return true;
    }

    /**
     * 取消暂停一个 Trigger
     *
     * @return 成功返回true
     */
    @Transactional
    public boolean resumeTrigger(TriggerKeyReq triggerKeyReq, AjaxMessage ajaxMessage) {
        Scheduler scheduler = QuartzManager.getScheduler();
        try {
            // 取消暂停触发器
            scheduler.resumeTrigger(TriggerKey.triggerKey(triggerKeyReq.getTriggerName(), triggerKeyReq.getTriggerGroup()));
        } catch (Throwable e) {
            log.error("取消暂停一个Trigger异常", e);
            ajaxMessage.setSuccess(false);
            ajaxMessage.setFailMessage("取消暂停一个Trigger失败");
            return false;
        }
        return true;
    }
}
