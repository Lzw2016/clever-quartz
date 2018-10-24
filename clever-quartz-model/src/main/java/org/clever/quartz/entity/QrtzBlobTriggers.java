package org.clever.quartz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 17:53 <br/>
 */
@Data
public class QrtzBlobTriggers implements Serializable {

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
     * 对于用户自定义的Trigger信息，无法提前设计字段，所以序列化后使用BLOB存储
     */
    private Byte[] blobData;
}
