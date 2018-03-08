package org.clever.quartz.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

/**
 * 作者：LiZW <br/>
 * 创建时间：2016-7-31 16:54 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddSimpleTriggerForJobRes extends BaseRequest {
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
     * Trigger key
     */
    @NotBlank(message = "TriggerName不能为空")
    @Length(max = 200, message = "TriggerName长度不能超过200")
    private String triggerName;

    /**
     * Trigger group名称
     */
    @NotBlank(message = "TriggerGroup不能为空")
    @Length(max = 200, message = "TriggerGroup长度不能超过200")
    private String triggerGroup;

    /**
     * Trigger 描述， .withDescription()方法传入的string
     */
    @Length(max = 250, message = "Job描述长度不能超过250")
    private String description;

    /**
     * 开始触发时间
     */
    @NotNull(message = "开始触发时间不能为空")
    private Date startTime;

    /**
     * 结束触发时间
     */
    private Date endTime;

    /**
     * 优先级
     */
    @Range(min = 1, max = 10, message = "Trigger优先级取值范围:1~10")
    private Integer priority;

    /**
     * 存储JobDataMap等
     */
    private Map<String, String> jobData;

    /**
     * 触发的时间间隔(毫秒)
     */
    @NotNull(message = "触发的时间间隔不能为空")
    @Range(min = 1, message = "触发的时间间隔大于0")
    private Integer interval;

    /**
     * 触发的次数
     */
    private Integer repeatCount;

    /**
     * Quartz的Misfire处理规则取值
     */
    private Integer misfireInstruction;
}
