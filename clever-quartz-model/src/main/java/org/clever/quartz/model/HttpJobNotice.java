package org.clever.quartz.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
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

    //--------------------------------------------------------------------------------------------------------------------------------------
    //     发送消息规则配置
    // -------------------------------------------------------------------------------------------------------------------------------------

    @ApiModelProperty("是否启用通知")
    @NotNull
    private boolean enable = true;

    @ApiModelProperty("任务执行最小时间,执行时间小于此值发送通知,值为空不比较(单位秒)")
    @Range(min = 0)
    private Long taskExecuteTimeMin;

    @ApiModelProperty("任务执行最最大时间,执行时间大于此值发送通知,值为空不比较(单位秒)")
    @Range(max = 60 * 60 * 24 * 365)
    private Long taskExecuteTimeMax = 60L;

    @ApiModelProperty("HTTP状态码,包含匹配的状态码就发送通知(*号匹配任何数字，如:2**匹配200~299状态码),值为空不比较")
    private Set<String> httpStatusCodeSet;

    @ApiModelProperty("返回body是否匹配当前这个正则表达式,匹配就发送通知,正则表达式为空则不匹配")
    private String bodyRegex;

    //--------------------------------------------------------------------------------------------------------------------------------------
    //     发送消息配置
    // -------------------------------------------------------------------------------------------------------------------------------------

    @ApiModelProperty("通知类型,取值: 邮件、短信、QQ消息、微信消息")
    @NotBlank(message = "通知类型不能为空")
    @Pattern(regexp = "email|sms|qq|wechat", message = "请求方式只能是:email、sms、qq、wechat")
    private String noticeType;

    @ApiModelProperty("消息接受者")
    @NotBlank(message = "消息接受者不能为空")

    private Set<String> sendTo;

    @ApiModelProperty("消息类容")
    private String messageContent;
}
