package org.clever.quartz.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 作者：LiZW <br/>
 * 创建时间：2016-7-30 14:54 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SaveJobDetailReq extends BaseRequest {
    private static final long serialVersionUID = 1L;

    /**
     * Job key
     */
    @NotBlank(message = "JobName不能为空")
    @Length(max = 200, message = "JobName长度不能超过200")
    private String jobName;

    /**
     * Job group 名称
     */
    @NotBlank(message = "JobGroup不能为空")
    @Length(max = 200, message = "JobGroup长度不能超过200")
    private String jobGroup;

    /**
     * Job描述， .withDescription()方法传入的string
     */
    @Length(max = 250, message = "Job描述长度不能超过250")
    private String description;

    /**
     * 实现Job的类名，trigger触发时调度此类的execute方法
     */
    @NotBlank(message = "JobClassName不能为空")
    @Length(max = 250, message = "JobClassName长度不能超过250")
    private String jobClassName;

    /**
     * Scheduler实例发生故障时，故障恢复节点会检测故障的Scheduler正在调度的任务是否需要recovery(复苏)，如果需要会添加一个只执行一次的simple trigger重新触发
     */
    @NotNull(message = "requestsRecovery不能为空")
    private Boolean requestsRecovery;

    /**
     * 存储JobDataMap
     */
    private Map<String, String> jobData;
}
