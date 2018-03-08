package org.clever.quartz.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 作者：LiZW <br/>
 * 创建时间：2016-7-29 14:34 <br/>
 */
@Data
public class QuartzJobDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Scheduler名称
     */
    private String schedName;

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

    /**
     * 存储JobDataMap等
     */
    private Map<String, Object> jobData;

    /**
     * 任务包含的触发器
     */
    private List<QuartzTriggers> triggersList;
}
