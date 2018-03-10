package org.clever.quartz.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Http 任务返回结果
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2017/6/5 10:15 <br/>
 */
@Data
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
    private Date startJobTime;

    /**
     * 任务结束时间
     */
    private Date endJobTime;

    /**
     * 任务执行异常-堆栈信息
     */
    private String exceptionStack;
}
