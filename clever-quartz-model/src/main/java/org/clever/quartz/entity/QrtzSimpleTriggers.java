package org.clever.quartz.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:32 <br/>
 */
@Table(name = "qrtz_simple_triggers")
@Data
public class QrtzSimpleTriggers implements Serializable {

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
     * 需要重复次数
     */
    private Long repeatCount;

    /**
     * 重复间隔
     */
    private Long repeatInterval;

    /**
     * 已经触发次数
     */
    private Long timesTriggered;
}
