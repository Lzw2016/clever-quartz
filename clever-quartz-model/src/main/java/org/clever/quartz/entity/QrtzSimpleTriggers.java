package org.clever.quartz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:32 <br/>
 */
@Data
public class QrtzSimpleTriggers implements Serializable {

    /**
     * Scheduler名称
     */
    @TableId
    private String schedulerName;

    /**
     * Trigger key
     */
    @TableId
    private String triggerName;

    /**
     * Trigger group 名称
     */
    @TableId
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
