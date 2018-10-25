package org.clever.quartz.model;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-25 19:22 <br/>
 */
public class HttpJobDataKeyConstant {

    /**
     * Http任务配置Key
     */
    public static final String HttpJobConfig = "HttpJobConfig";

    /**
     * Http任务通知配置key
     */
    public static final String HttpJobNotice = "HttpJobNotice";

    /**
     * Http扩展任务数据(JobData) -- 使用 http head 传递 json 字符串 Map<String, String>
     */
    public static final String HttpJobExtendData = "HttpJobExtendData";

    /**
     * 发送认证通知的消息数据 -- 使用 http head 传递 json 字符串 Map<String, String> <br />
     * 任务响应 http head 中存在此值就发送通知,优先级最高(相对HttpJobNotice配置)
     */
    public static final String NoticeMessageData = "NoticeMessageData";
}
