package org.clever.quartz.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体类，对应表qrtz_trigger_log(Trigger触发日志表)<br/>
 * <p/>
 * 作者：lizw <br/>
 * 创建时间：2017/6/1 9:38 <br/>
 */
@Table(name = "qrtz_trigger_log")
@Data
public class QrtzTriggerLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 监听器名称
     */
    private String listenerName;

    /**
     * Scheduler名称
     */
    private String schedName;

    /**
     * Scheduler实例的唯一标识，配置文件中的Instance Id
     */
    private String instanceName;

    /**
     * Job key
     */
    private String jobName;

    /**
     * Job group 名称
     */
    private String jobGroup;

    /**
     * Job类名称
     */
    private String jobClassName;

    /**
     * Trigger key
     */
    private String triggerName;

    /**
     * Trigger group名称
     */
    private String triggerGroup;

    /**
     * 开始触发时间
     */
    private Date startTime;

    /**
     * 触发完成时间
     */
    private Date endTime;

    /**
     * 用时(ms)
     */
    private Long processTime;

    /**
     * 上一次触发时间
     */
    private Date preRunTime;

    /**
     * 下一次触发时间
     */
    private Date nextRunTime;

    /**
     * 触发次数
     */
    private Integer runCount;

    /**
     * 触发节点IP,可能有多个(‘;’分隔)
     */
    private String ipAddress;

    /**
     * 是否错过了触发（0：否；1：是）
     */
    private Character misFired;

    /**
     * 执行前的JobDataMap数据
     */
    private String beforeJobData;

    /**
     * 执行后的JobDataMap数据
     */
    private String afterJobData;

    /**
     * 触发指令码
     */
    private String triggerInstructionCode;

    /**
     * 触发指令码说明
     */
    private String instrCode;
}
