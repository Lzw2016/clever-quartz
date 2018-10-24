package org.clever.quartz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:19 <br/>
 */
@Data
public class QrtzCalendars implements Serializable {

    /**
     * Scheduler名称
     */
    @TableId
    private String schedulerName;

    /**
     * Calendar 名称
     */
    @TableId
    private String calendarName;

    /**
     * Calendar 数据
     */
    private Byte[] calendar;

}
