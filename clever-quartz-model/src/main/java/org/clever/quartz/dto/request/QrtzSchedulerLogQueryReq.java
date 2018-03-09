package org.clever.quartz.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

import java.util.Date;

/**
 * 作者：LiZW <br/>
 * 创建时间：2016-8-16 18:20 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QrtzSchedulerLogQueryReq extends QueryByPage {
    private static final long serialVersionUID = 1L;

    /**
     * 调度器名称
     */
    private String schedulerName;

    /**
     * 调度器ID
     */
    private String instanceName;

    /**
     * 触发事件调用的方法
     */
    private String methodName;

    /**
     * 记录时间-起始值
     */
    private Date logTimeStart;

    /**
     * 记录时间-结束值
     */
    private Date logTimeEnd;
}
