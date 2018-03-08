package org.clever.quartz.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Http 任务返回结果
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2017/6/5 10:15 <br/>
 */
public class HttpJobResult implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 任务执行 状态
     */
    private String status;

    /**
     * 任务响应消息
     */
    private String message;

    /**
     * 任务返回数据 - 可以存储任务执行的服务器节点等等信息
     */
    private Map<String, String> resultData;

    /**
     * 任务执行开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date startJobTime;

    /**
     * 任务结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date endJobTime;

    /**
     * 任务执行异常-堆栈信息
     */
    private String exceptionStack;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getResultData() {
        return resultData;
    }

    public void setResultData(Map<String, String> resultData) {
        this.resultData = resultData;
    }

    public Date getStartJobTime() {
        return startJobTime;
    }

    public void setStartJobTime(Date startJobTime) {
        this.startJobTime = startJobTime;
    }

    public Date getEndJobTime() {
        return endJobTime;
    }

    public void setEndJobTime(Date endJobTime) {
        this.endJobTime = endJobTime;
    }

    public String getExceptionStack() {
        return exceptionStack;
    }

    public void setExceptionStack(String exceptionStack) {
        this.exceptionStack = exceptionStack;
    }
}
