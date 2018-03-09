package org.clever.quartz.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 作者：LiZW <br/>
 * 创建时间：2016-7-31 22:25 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TriggerKeyReq extends BaseRequest {
    private static final long serialVersionUID = 1L;

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
}
