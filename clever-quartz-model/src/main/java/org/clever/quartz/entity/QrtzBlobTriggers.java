package org.clever.quartz.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 17:53 <br/>
 */
@Table(name = "qrtz_blob_triggers")
@Data
public class QrtzBlobTriggers implements Serializable {

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
     * 对于用户自定义的Trigger信息，无法提前设计字段，所以序列化后使用BLOB存储
     */
    private Byte[] blobData;
}
