package org.clever.quartz.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 实体类，对应表qrtz_triggers(存储已配置的Trigger的信息)<br/>
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2017/6/1 9:45 <br/>
 */
@Table(name = "qrtz_triggers")
@Data
public class QrtzTriggers implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Scheduler名称
     */
    @Id
    private String schedName;

    /**
     * Trigger key
     */
    @Id
    private String triggerName;

    /**
     * Trigger group名称
     */
    @Id
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
    private Long nextFireTime;

    /**
     * 上一次触发时间，默认-1
     */
    private Long prevFireTime;

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
    private Long startTime;

    /**
     * Trigger结束时间
     */
    private Long endTime;

    /**
     * Trigger关联的Calendar name
     */
    private String calendarName;

    /**
     * misfire规则id
     */
    private Short misfireInstr;

    /**
     * 存储Trigger的JobDataMap等
     */
    private Byte[] jobData;
}
