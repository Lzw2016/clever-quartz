package org.clever.quartz.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

import java.util.Date;

/**
 * 作者：LiZW <br/>
 * 创建时间：2016-8-16 15:46 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JobLogQueryReq extends QueryByPage {
    private static final long serialVersionUID = 1L;

    /**
     * 调度器名称
     */
    private String schedName;

    /**
     * 调度器ID
     */
    private String instanceName;

    /**
     * 任务分组
     */
    private String jobGroup;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务实现类
     */
    private String jobClassName;

    /**
     * 开始触发时间-起始值
     */
    private Date startTimeByStart;

    /**
     * 开始触发时间-结束值
     */
    private Date startTimeByEnd;

    /**
     * 处理时间-最小值
     */
    private Long processTimeByMin;

    /**
     * 处理时间-最大值
     */
    private Long processTimeByMax;
}
