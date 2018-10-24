package org.clever.quartz.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-10 11:48 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JobDetailsRes extends BaseResponse {

    /**
     * Scheduler名称
     */
    private String schedulerName;

    /**
     * Job key
     */
    private String jobName;

    /**
     * Job group 名称
     */
    private String jobGroup;

    /**
     * Job描述， .withDescription()方法传入的string
     */
    private String description;

    /**
     * 实现Job的类名，trigger触发时调度此类的execute方法
     */
    private String jobClassName;

    /**
     * 为true时，Job相关的trigger完成以后，Job数据继续保留
     */
    private boolean isDurable;

    /**
     * 是否不允许并发，为true时，如果下一次的触发事件到了，而上一次的job执行还未结束，则后续的触发会放入队列等待
     */
    private boolean isNonconcurrent;

    /**
     * 是否在多次调度之间更新JobDataMap
     */
    private boolean isUpdateData;

    /**
     * Scheduler实例发生故障时，故障恢复节点会检测故障的Scheduler正在调度的任务是否需要recovery(复苏)，如果需要会添加一个只执行一次的simple trigger重新触发
     */
    private boolean requestsRecovery;

    // =============================== Triggers

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
}
