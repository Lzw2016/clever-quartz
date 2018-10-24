package org.clever.quartz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体类，对应表qrtz_job_log(Job执行日志表)<br/>
 * <p>
 * 作者：LiZW <br/>
 * 创建时间：2016-8-3 17:46 <br/>
 */
@Data
public class QrtzJobLog implements Serializable {
    /**
     * 任务执行状态(0:正在运行,1:成功;2:失败)
     */
    public static final char STATUS_RUNNING = '0';
    /**
     * 任务执行状态(0:正在运行,1:成功;2:失败)
     */
    public static final char STATUS_SUCCESS = '1';
    /**
     * 任务执行状态(0:正在运行,1:成功;2:失败)
     */
    public static final char STATUS_FAIL = '2';
    private static final long serialVersionUID = 1L;
    /**
     * 数据ID
     */
    @TableId
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
     * 开始执行时间
     */
    private Date startTime;

    /**
     * 执行结束时间
     */
    private Date endTime;

    /**
     * 执行用时(ms)
     */
    private Long processTime;

    /**
     * 上一次执行时间
     */
    private Date preRunTime;

    /**
     * 下一次执行时间
     */
    private Date nextRunTime;

    /**
     * 执行次数
     */
    private Integer runCount;

    /**
     * 执行节点IP,可能有多个(‘;’分隔)
     */
    private String ipAddress;

    /**
     * 任务执行状态(0:正在运行,1:成功;2:失败)
     */
    private Character status;

    /**
     * 异常信息
     */
    private String exceptionInfo;

    /**
     * 是否被否决（0：否；1：是）
     */
    private Character isVeto;

    /**
     * 执行前的JobDataMap数据
     */
    private String beforeJobData;

    /**
     * 执行后的JobDataMap数据
     */
    private String afterJobData;
}
