package org.clever.quartz.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * 作者：LiZW <br/>
 * 创建时间：2016-7-30 14:08 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ValidatorCronRes extends BaseRequest {
    private static final long serialVersionUID = 1L;

    /**
     * cron表达式
     */
    @NotBlank(message = "cron表达式不能为空")
    private String cron;

    /**
     * 获取cron表达式表示时间数量
     */
    @Range(min = 1, max = 20, message = "获取cron表达式表示时间数量取值范围:1~20")
    @NotNull(message = "获取cron表达式表示时间数量不能为空")
    private int num = 10;
}
