package org.clever.quartz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:27 <br/>
 */
@Data
public class QrtzLocks implements Serializable {

    /**
     * Scheduler名称，同一集群下的Scheduler实例名称相同，Instance_Id不同
     */
    @TableId
    private String schedName;

    /**
     * 锁名称，TRIGGER_ACCESS，STATE_ACCESS，JOB_ACCESS，CALENDAR_ACCESS，MISFIRE_ACCESS
     */
    @TableId
    private String lockName;

}
