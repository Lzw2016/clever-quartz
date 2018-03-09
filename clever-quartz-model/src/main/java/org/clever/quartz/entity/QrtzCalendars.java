package org.clever.quartz.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:19 <br/>
 */
@Table(name = "qrtz_calendars")
@Data
public class QrtzCalendars implements Serializable {

    /**
     * Scheduler名称
     */
    @Id
    private String schedName;

    /**
     * Calendar 名称
     */
    @Id
    private String calendarName;

    /**
     * Calendar 数据
     */
    private Byte[] calendar;

}
