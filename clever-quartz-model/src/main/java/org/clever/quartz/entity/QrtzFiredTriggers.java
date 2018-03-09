package org.clever.quartz.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:25 <br/>
 */
@Table(name = "qrtz_fired_triggers")
@Data
public class QrtzFiredTriggers implements Serializable {

    /**
     * Scheduler名称
     */
    @Id
    private String schedName;

    /**
     * 条目号
     */
    @Id
    private String entryId;

    /**
     * Trigger key
     */
    private String triggerName;

    /**
     * Trigger group 名称
     */
    private String triggerGroup;

    /**
     * Scheduler实例的唯一标识（应该是完成这次调度的Scheduler标识，待多实例环境测试验证）
     */
    private String instanceName;

    /**
     * 触发时间
     */
    private Long firedTime;

    /**
     * （疑似下一次触发时间，待验证）
     */
    private Long schedTime;

    /**
     * Trigger 优先级
     */
    private Integer priority;

    /**
     * Trigger状态
     */
    private String state;

    /**
     * Job key
     */
    private String jobName;

    /**
     * Job group 名称
     */
    private String jobGroup;

    /**
     * 是否不允许并发
     */
    private String isNonconcurrent;

    /**
     * Scheduler实例发生故障时，故障恢复节点会检测故障的Scheduler正在调度的任务是否需要recovery，如果需要会添加一个只执行一次的simple trigger重新触发
     */
    private String requestsRecovery;

}
