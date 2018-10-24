package org.clever.quartz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:29 <br/>
 */
@Data
public class QrtzPausedTriggerGrps implements Serializable {

    /**
     * Scheduler名称
     */
    @TableId
    private String schedulerName;

    /**
     * Trigger group 名称
     */
    @TableId
    private String triggerGroup;
}
