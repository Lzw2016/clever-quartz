package org.clever.quartz.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-10 11:33 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JobKeyRes extends BaseResponse {
    /**
     * Job key
     */
    private String jobName;

    /**
     * Job group 名称
     */
    private String jobGroup;
}
