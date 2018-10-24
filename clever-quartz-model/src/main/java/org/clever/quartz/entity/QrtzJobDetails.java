package org.clever.quartz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:26 <br/>
 */
@Data
public class QrtzJobDetails implements Serializable {

    /**
     * Scheduler名称
     */
    @TableId
    private String schedulerName;

    /**
     * Job key
     */
    @TableId
    private String jobName;

    /**
     * Job group 名称
     */
    @TableId
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
    private String isDurable;

    /**
     * 是否不允许并发，为true时，如果下一次的触发事件到了，而上一次的job执行还未结束，则后续的触发会放入队列等待
     */
    private String isNonconcurrent;

    /**
     * 是否在多次调度之间更新JobDataMap
     */
    private String isUpdateData;

    /**
     * Scheduler实例发生故障时，故障恢复节点会检测故障的Scheduler正在调度的任务是否需要recovery(复苏)，如果需要会添加一个只执行一次的simple trigger重新触发
     */
    private String requestsRecovery;

    /**
     * 存储JobDataMap等
     */
    private Byte[] jobData;

}
