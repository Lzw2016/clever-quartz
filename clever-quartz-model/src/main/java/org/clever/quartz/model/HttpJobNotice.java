package org.clever.quartz.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Map;

/**
 * 任务消息通知信息
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2017/6/5 10:36 <br/>
 */
@Data
public class HttpJobNotice implements Serializable {
    public static final String HTTP_JOB_NOTICE_KEY = "HttpJobNotice";
    private static final long serialVersionUID = 1L;
    /**
     * 通知类型,取值: 邮件、短信、QQ消息、微信消息
     */
    @NotBlank(message = "通知类型不能为空")
    @Pattern(regexp = "邮件|短信|QQ消息|微信消息", message = "请求方式只能是:邮件、短信、QQ消息、微信消息")
    private String noticeType;

    /**
     * 消息接受者
     */
    @NotBlank(message = "消息接受者不能为空")
    private String sendTo;

    /**
     * 消息模版
     */
    @NotBlank(message = "消息模版不能为空")
    private String messageTemplate;

    /**
     * 消息数据
     */
    private Map<String, String> messageData;
}
