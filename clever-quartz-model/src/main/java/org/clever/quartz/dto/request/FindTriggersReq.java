package org.clever.quartz.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-29 9:27 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FindTriggersReq extends QueryByPage {
    private String triggerGroup;
    private String triggerName;
    private String jobGroup;
    private String jobName;
}
