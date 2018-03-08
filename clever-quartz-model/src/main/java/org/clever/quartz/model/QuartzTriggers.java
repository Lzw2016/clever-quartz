package org.clever.quartz.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 作者：LiZW <br/>
 * 创建时间：2016-7-29 14:41 <br/>
 */
@Data
public class QuartzTriggers implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Scheduler名称
     */
    private String schedName;

    /**
     * Trigger key
     */
    private String triggerName;

    /**
     * Trigger group名称
     */
    private String triggerGroup;

    /**
     * Job key
     */
    private String jobName;

    /**
     * Job group 名称
     */
    private String jobGroup;

    /**
     * Trigger 描述， .withDescription()方法传入的string
     */
    private String description;

    /**
     * 下一次触发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date nextFireTime;

    /**
     * 上一次触发时间，默认-1
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date prevFireTime;

    /**
     * Trigger 优先级，默认5
     */
    private Integer priority;

    /**
     * Trigger状态，PAUSED_BLOCKED:停止_阻塞; PAUSED:停止; WAITING:等待执行; ACQUIRED:已获得; EXECUTING:执行中; COMPLETE:完成; BLOCKED:阻塞; ERROR:错误; DELETED:已删除
     */
    private String triggerState;

    /**
     * Cron 或 Simple
     */
    private String triggerType;

    /**
     * Trigger开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date startTime;

    /**
     * Trigger结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date endTime;

    /**
     * Trigger关联的Calendar name
     */
    private String calendarName;

    /**
     * misfire规则id
     */
    private Integer misfireInstr;

    /**
     * 存储Trigger的JobDataMap等
     */
    private Map<String, Object> jobData;

    /*===================== SimpleTrigger =====================*/
    /**
     * 需要重复次数
     */
    private Integer repeatCount;

    /**
     * 重复触发时间间隔(毫秒)
     */
    private Long repeatInterval;

    /**
     * 已经触发的次数
     */
    private Integer timesTriggered;

    /*===================== CronTrigger =====================*/
    /**
     * cron表达式
     */
    private String cronEx;

    /**
     * 时区ID
     */
    private String timeZoneId;
}
