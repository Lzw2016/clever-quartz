package org.clever.quartz.jobs;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.exception.ExceptionUtils;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.quartz.model.HttpJobData;
import org.clever.quartz.model.HttpJobNotice;
import org.clever.quartz.model.HttpJobResult;
import org.clever.quartz.utils.HttpClientUtils;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.util.Map;

/**
 * 使用Http请求调用服务接口
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2017/6/1 11:34 <br/>
 */
@Slf4j
public class HttpServiceJob implements Job {
    private static final String HTTP_JOB_RESULT_KEY = "HttpJobResult";

    // QuartzJobBean
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        Object object = jobDetail.getJobDataMap().get(HttpJobData.HTTP_JOB_DATA_KEY);
        if (object == null) {
            RuntimeException exception = new RuntimeException("[HttpServiceJob]任务数据不存在");
            log.error(exception.getMessage(), exception);
            throw exception;
        }
        if (!(object instanceof HttpJobData)) {
            RuntimeException exception = new RuntimeException("[HttpServiceJob]任务数据类型不正确,类型[" + object.getClass() + "]");
            log.error(exception.getMessage(), exception);
            throw exception;
        }
        HttpJobData httpJobData = (HttpJobData) object;

        HttpJobNotice notice = null;
        object = jobDetail.getJobDataMap().get(HttpJobNotice.HTTP_JOB_NOTICE_KEY);
        if (object != null) {
            if (!(object instanceof HttpJobNotice)) {
                RuntimeException exception = new RuntimeException("[HttpServiceJob]通知数据类型不正确,类型[" + object.getClass() + "]");
                log.error(exception.getMessage(), exception);
                throw exception;
            }
            notice = (HttpJobNotice) object;
        }

//        HttpJobResult httpJobResult;
//        try {
//            httpJobResult = sendRequest(httpJobData, jobDetail);
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//            throw ExceptionUtils.unchecked(e);
//        }
//        if (httpJobResult == null) {
//            // 服务调用失败
//        }
//        if (notice != null) {
//            // 发送通知
//        }
//        httpJobResult.
    }

//    /**
//     * 发送Http请求
//     */
//    private HttpJobResult sendRequest(HttpJobData httpJobData, JobDetail jobDetail) throws IOException {
//        HttpJobResult httpJobResult = null;
//        OkHttpClient okHttpClient = HttpClientUtils.getOkHttpClient();
//        Request.Builder requestBuilder = new Request.Builder();
//        requestBuilder.url(httpJobData.getUrl());
//        if (httpJobData.getHeaders() != null) {
//            for (Map.Entry<String, String> entry : httpJobData.getHeaders().entrySet()) {
//                requestBuilder.addHeader(entry.getKey(), entry.getValue());
//            }
//        }
//
//        RequestBody requestBody = null;
//        if (httpJobData.getFormBody() != null) {
//            FormBody.Builder formBodyBuilder = new FormBody.Builder();
//            for (Map.Entry<String, String> entry : httpJobData.getFormBody().entrySet()) {
//                formBodyBuilder.add(entry.getKey(), entry.getValue());
//            }
//            requestBody = formBodyBuilder.build();
//        }
//        if (StringUtils.isNotBlank(httpJobData.getJsonBody())) {
//            requestBody = RequestBody.create(HttpClientUtils.JSON, httpJobData.getJsonBody());
//        }
//        // TODO httpJobData.getMethod() ?
//        if (requestBody != null) {
//            requestBuilder.method(httpJobData.getMethod().toUpperCase(), requestBody);
//        }
//
//        Request request = requestBuilder.build();
//        Response response = okHttpClient.newCall(request).execute();
//        ResponseBody responseBody = response.body();
//        jobDetail.getJobDataMap().put("httpStatus", response.code());
//        String json = null;
//        if (responseBody != null) {
//            json = responseBody.string();
//        }
//        if (StringUtils.isNotBlank(json)) {
//            jobDetail.getJobDataMap().put(HTTP_JOB_RESULT_KEY, json);
//            httpJobResult = JacksonMapper.nonEmptyMapper().fromJson(json, HttpJobResult.class);
//        }
//        if (log.isInfoEnabled()) {
//            String tmp = "\r\n" +
//                    "#=======================================================================================================================#\r\n" +
//                    "# 任务组名：[" + jobDetail.getKey().getGroup() + "] | 任务名称：[" + jobDetail.getKey().getName() + "]\r\n" +
//                    "# 请求地址：[" + httpJobData.getUrl() + "]\r\n" +
//                    "# HTTP响应状态码：[" + response.code() + "] | 响应消息：[" + response.message() + "]\r\n" +
//                    "# 响应数据：[" + json + "]\r\n" +
//                    "#=======================================================================================================================#\r\n";
//            log.info(tmp);
//        }
//        return httpJobResult;
//    }
}
