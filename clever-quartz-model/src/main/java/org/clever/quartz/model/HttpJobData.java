package org.clever.quartz.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Map;

/**
 * Http 任务数据信息
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2017/6/5 9:14 <br/>
 */
public class HttpJobData implements Serializable {
    public static final String HTTP_JOB_DATA_KEY = "HttpJobData";
    private static final long serialVersionUID = 1L;
    /**
     * 请求地址
     */
    @NotBlank(message = "请求地址不能为空")
    private String url;

    /**
     * 请求方式包括 OPTIONS、GET、HEAD、POST、PUT、DELETE、TRACE，常用的是GET和POST
     */
    @Pattern(regexp = "OPTIONS|GET|HEAD|POST|PUT|DELETE|TRACE", message = "请求方式只能是:OPTIONS、GET、HEAD、POST、PUT、DELETE、TRACE")
    private String method = "POST";

    /**
     * 请求头信息
     */
    private Map<String, String> headers;

    /**
     * 表单数据
     */
    private Map<String, String> formBody;

    /**
     * 请求正文 - application/json 类型
     */
    private String jsonBody;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getFormBody() {
        return formBody;
    }

    public void setFormBody(Map<String, String> formBody) {
        this.formBody = formBody;
    }

    public String getJsonBody() {
        return jsonBody;
    }

    public void setJsonBody(String jsonBody) {
        this.jsonBody = jsonBody;
    }
}
