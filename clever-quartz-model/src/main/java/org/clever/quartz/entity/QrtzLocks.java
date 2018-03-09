package org.clever.quartz.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:27 <br/>
 */
@Table(name = "qrtz_locks")
@Data
public class QrtzLocks implements Serializable {

    /**
     * Scheduler名称，同一集群下的Scheduler实例名称相同，Instance_Id不同
     */
    @Id
    private String schedName;

    /**
     * 锁名称，TRIGGER_ACCESS，STATE_ACCESS，JOB_ACCESS，CALENDAR_ACCESS，MISFIRE_ACCESS
     */
    @Id
    private String lockName;

}
