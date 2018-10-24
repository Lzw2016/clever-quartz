package org.clever.quartz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体类，对应表qrtz_scheduler_log(Scheduler调度日志表)<br/>
 * <p/>
 * 作者：lizw <br/>
 * 创建时间：2017/6/1 9:31 <br/>
 */
@Data
public class QrtzSchedulerLog implements Serializable {
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
     * 触发事件调用的方法
     */
    private String methodName;

    /**
     * 触发事件记录的日志数据
     */
    private String logData;

    /**
     * 触发节点IP,可能有多个(‘;’分隔)
     */
    private String ipAddress;

    /**
     * 记录时间
     */
    private Date logTime;
}
