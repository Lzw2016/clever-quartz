package org.clever.quartz.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/6/7 10:24 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PauseOrResumeJobVo extends BaseRequest {
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
     * Job状态: pause(暂停)、resume(继续运行)、trigger(立即运行)
     */
    @NotBlank(message = "status不能为空")
    @Pattern(regexp = "pause|resume|trigger", message = "Job状态只能是: pause(暂停)、resume(继续运行)、trigger(立即运行)")
    private String status;
}
