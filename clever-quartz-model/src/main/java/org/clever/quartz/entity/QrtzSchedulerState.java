package org.clever.quartz.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:30 <br/>
 */
@Table(name = "qrtz_scheduler_state")
@Data
public class QrtzSchedulerState implements Serializable {

    /**
     * Scheduler名称
     */
    @Id
    private String schedName;

    /**
     * Scheduler实例的唯一标识，配置文件中的Instance Id
     */
    @Id
    private String instanceName;

    /**
     * 最后检入时间
     */
    private Long lastCheckinTime;

    /**
     * Scheduler 实例检入到数据库中的频率，单位毫秒
     */
    private Long checkinInterval;
}
