package org.clever.quartz.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/6/7 11:45 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FindJobDetailReq extends BaseRequest {
    private static final long serialVersionUID = 1L;

    private String jobName;

    private String jobGroup;
}
