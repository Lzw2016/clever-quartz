package org.clever.quartz.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Set;

/**
 * Http任务通知配置<br />
 * 定义任务执行出了哪一种问题(2xx,4xx,5xx,timeout)才发送告警(内置统一告警模版)<br />
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2017/6/5 10:36 <br/>
 */
@Data
public class HttpJobNotice implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String NoticeTypeValid = "email|sms|qq|wechat";

    /**
     * 通知数据Map Key - 请求method
     */
    public static final String NoticeDataMapKey_Method = "method";

    /**
     * 通知数据Map Key - 请求url
     */
    public static final String NoticeDataMapKey_Url = "url";

    /**
     * 通知数据Map Key - 响应http status code
     */
    public static final String NoticeDataMapKey_Code = "code";
    /**
     * 通知数据Map Key - 响应http status message
     */
    public static final String NoticeDataMapKey_Message = "message";
    /**
     * 通知数据Map Key - 响应body
     */
    public static final String NoticeDataMapKey_Body = "body";

    /**
     * 通知数据Map Key - 响应jobGroup
     */
    public static final String NoticeDataMapKey_JobGroup = "jobGroup";

    /**
     * 通知数据Map Key - 响应jobName
     */
    public static final String NoticeDataMapKey_JobName = "jobName";

    //--------------------------------------------------------------------------------------------------------------------------------------
    //     发送消息规则配置
    // -------------------------------------------------------------------------------------------------------------------------------------

    @ApiModelProperty("是否启用通知,启用:当任务调用发生异常或者任务调用满足规则就会发送告警通知")
    @NotNull
    private boolean enable = true;

    @ApiModelProperty("任务执行最小时间,执行时间小于此值发送通知,值为空不比较(单位毫秒)")
    @Range(min = 0)
    private Long taskExecuteTimeMin;

    @ApiModelProperty("任务执行最最大时间,执行时间大于此值发送通知,值为空不比较(单位毫秒)")
    @Range(max = 1000 * 60 * 10)
    private Long taskExecuteTimeMax = 1000 * 60L;

    @ApiModelProperty("HTTP状态码正则表达式,匹配就发送通知,值为空不比较")
    @Pattern(regexp = "[\\s\\S]*\\S+[\\s\\S]*", message = "不能是空")
    private String httpStatusCodeRegex;

    @ApiModelProperty("返回body是否匹配当前这个正则表达式,匹配就发送通知,正则表达式为空则不匹配")
    @Pattern(regexp = "[\\s\\S]*\\S+[\\s\\S]*", message = "不能是空")
    private String bodyRegex;

    //--------------------------------------------------------------------------------------------------------------------------------------
    //     发送消息配置
    // -------------------------------------------------------------------------------------------------------------------------------------

    @ApiModelProperty("通知类型,取值: 邮件、短信、QQ消息、微信消息")
    @Pattern(regexp = NoticeTypeValid, message = "请求方式只能是:email、sms、qq、wechat")
    private String noticeType;

    @ApiModelProperty("消息接受者")
    private Set<String> sendTo;

    @ApiModelProperty("消息类容(可选)")
    private String messageContent;

    //--------------------------------------------------------------------------------------------------------------------------------------
    //     方法
    // -------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 业务验证配置是否正确
     */
    public void valid() {
        if (!enable) {
            return;
        }
        if (StringUtils.isBlank(noticeType) || !noticeType.matches(NoticeTypeValid)) {
            throw new BusinessException("通知类型参数错误:[" + noticeType + "]");
        }
        if (sendTo == null || sendTo.size() <= 0 || sendTo.stream().filter(StringUtils::isNotBlank).count() <= 0) {
            throw new BusinessException("消息接受者参数错误:[" + sendTo + "]");
        }
//        if (taskExecuteTimeMin == null && taskExecuteTimeMax == null && StringUtils.isBlank(httpStatusCodeRegex) && StringUtils.isBlank(bodyRegex)) {
//            throw new BusinessException("发送消息规则配置错误:[配置不能都为空]");
//        }
    }
}
