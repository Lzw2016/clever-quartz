package org.clever.quartz.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:20 <br/>
 */
@Table(name = "qrtz_cron_triggers")
@Data
public class QrtzCronTriggers implements Serializable {

    /**
     * Scheduler名称
     */
    @Id
    private String schedName;

    /**
     * Trigger key
     */
    @Id
    private String triggerName;

    /**
     * Trigger group 名称
     */
    @Id
    private String triggerGroup;

    /**
     * 调度规则
     */
    private String cronExpression;

    /**
     * 时区
     */
    private String timeZoneId;
}
