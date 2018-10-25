package org.clever.quartz.jobs;

import lombok.extern.slf4j.Slf4j;
import org.clever.quartz.model.HttpJobConfig;
import org.clever.quartz.model.HttpJobDataKeyConstant;
import org.clever.quartz.model.HttpJobNotice;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

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
        Object object = jobDetail.getJobDataMap().get(HttpJobDataKeyConstant.HttpJobConfig);
        if (object == null) {
            RuntimeException exception = new RuntimeException("[HttpServiceJob]任务数据不存在");
            log.error(exception.getMessage(), exception);
            throw exception;
        }
        if (!(object instanceof HttpJobConfig)) {
            RuntimeException exception = new RuntimeException("[HttpServiceJob]任务数据类型不正确,类型[" + object.getClass() + "]");
            log.error(exception.getMessage(), exception);
            throw exception;
        }
        HttpJobConfig httpJobConfig = (HttpJobConfig) object;

        HttpJobNotice notice = null;
        object = jobDetail.getJobDataMap().get(HttpJobDataKeyConstant.HttpJobNotice);
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
//            httpJobResult = sendRequest(httpJobConfig, jobDetail);
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
//    private HttpJobResult sendRequest(HttpJobConfig httpJobConfig, JobDetail jobDetail) throws IOException {
//        HttpJobResult httpJobResult = null;
//        OkHttpClient okHttpClient = HttpClientUtils.getOkHttpClient();
//        Request.Builder requestBuilder = new Request.Builder();
//        requestBuilder.url(httpJobConfig.getUrl());
//        if (httpJobConfig.getHeaders() != null) {
//            for (Map.Entry<String, String> entry : httpJobConfig.getHeaders().entrySet()) {
//                requestBuilder.addHeader(entry.getKey(), entry.getValue());
//            }
//        }
//
//        RequestBody requestBody = null;
//        if (httpJobConfig.getFormBody() != null) {
//            FormBody.Builder formBodyBuilder = new FormBody.Builder();
//            for (Map.Entry<String, String> entry : httpJobConfig.getFormBody().entrySet()) {
//                formBodyBuilder.add(entry.getKey(), entry.getValue());
//            }
//            requestBody = formBodyBuilder.build();
//        }
//        if (StringUtils.isNotBlank(httpJobConfig.getJsonBody())) {
//            requestBody = RequestBody.create(HttpClientUtils.JSON, httpJobConfig.getJsonBody());
//        }
//        // TODO httpJobConfig.getMethod() ?
//        if (requestBody != null) {
//            requestBuilder.method(httpJobConfig.getMethod().toUpperCase(), requestBody);
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
//                    "# 请求地址：[" + httpJobConfig.getUrl() + "]\r\n" +
//                    "# HTTP响应状态码：[" + response.code() + "] | 响应消息：[" + response.message() + "]\r\n" +
//                    "# 响应数据：[" + json + "]\r\n" +
//                    "#=======================================================================================================================#\r\n";
//            log.info(tmp);
//        }
//        return httpJobResult;
//    }
}
