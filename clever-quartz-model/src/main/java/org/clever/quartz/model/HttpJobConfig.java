package org.clever.quartz.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Map;

/**
 * Http 任务数据信息
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2017/6/5 9:14 <br/>
 */
@Data
public class HttpJobConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 请求方式包括 GET|POST|PUT|DELETE|HEAD|PATCH，常用的是GET和POST
     */
    @Pattern(regexp = "GET|POST|PUT|DELETE|HEAD|PATCH", message = "请求方式只能是:GET|POST|PUT|DELETE|HEAD|PATCH")
    private String method = "POST";

    /**
     * 请求地址
     */
    @NotBlank(message = "请求地址不能为空")
    private String url;

    /**
     * 请求头信息
     */
    private Map<String, String> headers;

    /**
     * Url查询参数
     */
    private Map<String, String> params;

    /**
     * 请求正文 - application/json 类型
     */
    private String jsonBody;

    //--------------------------------------------------------------------------------------------------------------------------------------
    //     方法
    // -------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 业务验证配置是否正确
     */
    public void valid() {
        if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method)) {
            if (StringUtils.isBlank(jsonBody)) {
                throw new BusinessException("jsonBody参数不能为空");
            }
        }
    }
}
