package org.clever.quartz.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/6/7 11:45 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FindJobDetailReq extends QueryByPage {
    private static final long serialVersionUID = 1L;

    /**
     * Job key
     */
    private String jobName;

    /**
     * Job group 名称
     */
    private String jobGroup;

    /**
     * 实现Job的类名，trigger触发时调度此类的execute方法
     */
    private String jobClassName;
}
