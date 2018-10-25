package org.clever.quartz.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.clever.quartz.model.HttpJobData;
import org.clever.quartz.model.HttpJobNotice;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

/**
 * 新增一个HttpJob
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2017/6/5 13:55 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddHttpServiceJobReq extends BaseRequest {
    private static final long serialVersionUID = 1L;

    //--------------------------------------------------------------------------------------------------------------------------------------
    //     Job配置
    // -------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Job、Trigger 名称
     */
    @NotBlank(message = "JobName不能为空")
    @Length(max = 200, message = "JobName长度不能超过200")
    private String name;

    /**
     * Job、Trigger group名称
     */
    @NotBlank(message = "JobGroup不能为空")
    @Length(max = 200, message = "JobGroup长度不能超过200")
    private String group;

    /**
     * Job描述， .withDescription()方法传入的string
     */
    @Length(max = 250, message = "Job描述长度不能超过250")
    private String description;

    /**
     * Scheduler实例发生故障时，故障恢复节点会检测故障的Scheduler正在调度的任务是否需要recovery(复苏)，如果需要会添加一个只执行一次的simple trigger重新触发
     */
    private boolean requestsRecovery = true;

    //--------------------------------------------------------------------------------------------------------------------------------------
    //     触发器配置
    // -------------------------------------------------------------------------------------------------------------------------------------

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
     * cron表达式
     */
    @NotBlank(message = "Cron表达式不能为空")
    private String cron;

    /**
     * Quartz的Misfire处理规则取值
     */
    private Integer misfireInstruction;

    //--------------------------------------------------------------------------------------------------------------------------------------
    //     Http请求配置(本质都是JobData)
    // -------------------------------------------------------------------------------------------------------------------------------------

    @Valid
    @NotNull(message = "Http请求配置不能为空")
    private HttpJobData httpJobData;

    /**
     * 消息通知配置
     */
    @Valid
    private HttpJobNotice notice;

    /**
     * 任务扩展的数据
     */
    private Map<String, String> jobExtendData;
}
