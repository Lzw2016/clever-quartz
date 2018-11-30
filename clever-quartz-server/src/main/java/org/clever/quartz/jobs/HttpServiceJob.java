package org.clever.quartz.jobs;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.ConversionUtils;
import org.clever.common.utils.HttpUtils;
import org.clever.common.utils.exception.ExceptionUtils;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.quartz.model.HttpJobConfig;
import org.clever.quartz.model.HttpJobDataKeyConstant;
import org.clever.quartz.model.HttpJobNotice;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用Http请求调用服务接口
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2017/6/1 11:34 <br/>
 */
@SuppressWarnings("NullableProblems")
@Slf4j
public class HttpServiceJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        Object object = jobDetail.getJobDataMap().get(HttpJobDataKeyConstant.HttpJobConfig);
        if (object == null) {
            throw new JobExecutionException("[HttpServiceJob]任务数据不存在");
        }
        if (!(object instanceof HttpJobConfig)) {
            throw new JobExecutionException("[HttpServiceJob]任务数据类型不正确,类型[" + object.getClass() + "]");
        }
        HttpJobConfig httpJobConfig = (HttpJobConfig) object;
        HttpJobNotice notice = null;
        object = jobDetail.getJobDataMap().get(HttpJobDataKeyConstant.HttpJobNotice);
        if (object != null) {
            if (!(object instanceof HttpJobNotice)) {
                throw new JobExecutionException("[HttpServiceJob]通知数据类型不正确,类型[" + object.getClass() + "]");
            }
            notice = (HttpJobNotice) object;
        }
        try {
            sendRequest(httpJobConfig, notice, jobDetail);
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    /**
     * 发送Http请求
     */
    private void sendRequest(final HttpJobConfig httpJobConfig, final HttpJobNotice notice, final JobDetail jobDetail) {
        Request.Builder builder = HttpUtils.createRequestBuilder(httpJobConfig.getUrl(), httpJobConfig.getHeaders(), httpJobConfig.getParams());
        // 设置 扩展的 JobData
        builder.addHeader(
                HttpJobDataKeyConstant.HttpJobExtendData,
                JacksonMapper.nonEmptyMapper().toJson(jobDetail.getJobDataMap().get(HttpJobDataKeyConstant.HttpJobExtendData))
        );
        RequestBody requestBody = null;
        if (StringUtils.isNotBlank(httpJobConfig.getJsonBody())) {
            requestBody = RequestBody.create(MediaType.parse(HttpUtils.MediaType_Json), httpJobConfig.getJsonBody());
        }
        switch (httpJobConfig.getMethod().toUpperCase()) {
            case "GET":
                builder.get();
                break;
            case "POST":
                if (requestBody == null) {
                    throw new BusinessException("POST请求Body必须有值");
                }
                builder.post(requestBody);
                break;
            case "PUT":
                if (requestBody == null) {
                    throw new BusinessException("PUT请求Body必须有值");
                }
                builder.put(requestBody);
                break;
            case "DELETE":
                if (requestBody == null) {
                    builder.delete();
                } else {
                    builder.delete(requestBody);
                }
                break;
            case "HEAD":
                builder.head();
                break;
            case "PATCH":
                if (requestBody == null) {
                    throw new BusinessException("PATCH请求Body必须有值");
                }
                builder.patch(requestBody);
                break;
            default:
                throw new BusinessException("不支持的请求类型:[" + httpJobConfig.getMethod() + "]");
        }
        Request request = builder.build();
        // 发送请求
        final long startTime = System.currentTimeMillis();
        HttpUtils.enqueue(HttpUtils.getInner().getOkHttpClient(), request, new Callback() {
            // 请求失败
            @Override
            public void onFailure(Call call, IOException e) {
                log.warn("### HttpJob onFailure", e);
            }

            // 请求成功
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(Call call, Response response) {
                // 执行时间
                final long executeTime = System.currentTimeMillis() - startTime;
                // 读取响应扩展的 JobData
                String jsonHttpJobExtendData = response.header(HttpJobDataKeyConstant.HttpJobExtendData);
                if (StringUtils.isNotBlank(jsonHttpJobExtendData)) {
                    Map<String, String> tmp = JacksonMapper.nonEmptyMapper().fromJson(jsonHttpJobExtendData, Map.class);
                    // TODO 异步无法保证JobDataMap能更新成功
                    jobDetail.getJobDataMap().put(HttpJobDataKeyConstant.HttpJobExtendData, JacksonMapper.nonEmptyMapper().toJson(tmp));
                }
                // 读取响应body
                String body = null;
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    try {
                        body = responseBody.string();
                    } catch (IOException e) {
                        throw ExceptionUtils.unchecked(e);
                    }
                }
                // 读取响应http status code
                int code = response.code();
                // 读取响应http status message
                String message = response.message();
                // 打印日志
                printfLog(httpJobConfig, jobDetail, code, message, body, executeTime);
                // 发送通知
                sendNotice(httpJobConfig, notice, jobDetail, code, message, body, executeTime, response.header(HttpJobDataKeyConstant.NoticeMessageData), null);
            }
        });
    }

    /**
     * 发送通知
     */
    @SuppressWarnings("unchecked")
    private void sendNotice(
            final HttpJobConfig httpJobConfig,
            final HttpJobNotice notice,
            final JobDetail jobDetail,
            int code,
            String message,
            String body,
            long executeTime,
            String jsonNoticeMessageData,
            Throwable e) {
        boolean needSend = false;
        // 禁用就不发告警
        if (!notice.isEnable()) {
            return;
        }
        // 出现异常发送告警
        if (e != null) {
            needSend = true;
        }
        if (!needSend && notice.getTaskExecuteTimeMin() != null && executeTime < notice.getTaskExecuteTimeMin()) {
            needSend = true;
        }
        if (!needSend && notice.getTaskExecuteTimeMax() != null && executeTime > notice.getTaskExecuteTimeMax()) {
            needSend = true;
        }
        if (!needSend && StringUtils.isNotBlank(notice.getHttpStatusCodeRegex()) && ConversionUtils.toString(code).matches(notice.getHttpStatusCodeRegex())) {
            needSend = true;
        }
        if (!needSend && StringUtils.isNotBlank(notice.getBodyRegex()) && ConversionUtils.toString(body).matches(notice.getBodyRegex())) {
            needSend = true;
        }
        if (!needSend) {
            return;
        }
        // 发送告警
        Map<String, String> noticeDataMap = new HashMap<>();
        noticeDataMap.put(HttpJobNotice.NoticeDataMapKey_Method, httpJobConfig.getMethod());
        noticeDataMap.put(HttpJobNotice.NoticeDataMapKey_Url, httpJobConfig.getUrl());
        noticeDataMap.put(HttpJobNotice.NoticeDataMapKey_Code, ConversionUtils.toString(code));
        noticeDataMap.put(HttpJobNotice.NoticeDataMapKey_Message, message);
        noticeDataMap.put(HttpJobNotice.NoticeDataMapKey_Body, body);
        noticeDataMap.put(HttpJobNotice.NoticeDataMapKey_JobGroup, jobDetail.getKey().getGroup());
        noticeDataMap.put(HttpJobNotice.NoticeDataMapKey_JobName, jobDetail.getKey().getName());
        if (StringUtils.isNotBlank(jsonNoticeMessageData)) {
            Map<String, String> tmp = JacksonMapper.nonEmptyMapper().fromJson(jsonNoticeMessageData, Map.class);
            noticeDataMap.putAll(tmp);
        }
        // TODO 发送告警 notice.messageContent
        if (e == null) {
            log.info("### 发送告警 {}", noticeDataMap);
        } else {
            log.info("### 发送告警 {}", noticeDataMap, e);
        }
    }

    /**
     * 打印日志
     */
    private void printfLog(final HttpJobConfig httpJobConfig, final JobDetail jobDetail, int code, String message, String body, long executeTime) {
        if (log.isInfoEnabled()) {
            String tmp = "\r\n" +
                    "#=======================================================================================================================#\r\n" +
                    "# 任务组名：[" + jobDetail.getKey().getGroup() + "] | 任务名称：[" + jobDetail.getKey().getName() + "]\r\n" +
                    "# 请求地址：[" + httpJobConfig.getUrl() + "]\r\n" +
                    "# HTTP响应状态码：[" + code + "] | 响应消息：[" + message + "] | 执行时间：[" + executeTime + "ms]\r\n" +
                    "# 响应数据：[" + bodyOverflow(body) + "]\r\n" +
                    "#=======================================================================================================================#\r\n";
            log.info(tmp);
        }
    }

    private static String bodyOverflow(String body) {
        if (StringUtils.isBlank(body)) {
            return body;
        }
        int maxLength = 1000;
        int length = body.length();
        if (length <= maxLength) {
            return body.replace('\n', ' ').replace('\r', ' ');
        }
        body = body.substring(0, maxLength).replace('\n', ' ').replace('\r', ' ');
        return String.format("(%s char) %s......", length, body);
    }
}
