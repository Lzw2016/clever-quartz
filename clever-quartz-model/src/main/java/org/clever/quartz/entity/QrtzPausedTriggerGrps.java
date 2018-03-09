package org.clever.quartz.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:29 <br/>
 */
@Table(name = "qrtz_paused_trigger_grps")
@Data
public class QrtzPausedTriggerGrps implements Serializable {

    /**
     * Scheduler名称
     */
    @Id
    private String schedName;

    /**
     * Trigger group 名称
     */
    @Id
    private String triggerGroup;
}
