package org.clever.quartz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:30 <br/>
 */
@Data
public class QrtzSchedulerState implements Serializable {

    /**
     * Scheduler名称
     */
    @TableId
    private String schedulerName;

    /**
     * Scheduler实例的唯一标识，配置文件中的Instance Id
     */
    @TableId
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
