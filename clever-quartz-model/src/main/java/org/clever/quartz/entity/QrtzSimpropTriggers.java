package org.clever.quartz.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:33 <br/>
 */
@Table(name = "qrtz_simprop_triggers")
@Data
public class QrtzSimpropTriggers implements Serializable {

    /**
     * Scheduler名称
     */
    @Id
    private String schedName;

    /**
     * Trigger key
     */
    @Id
    private String triggerName;

    /**
     * Trigger group 名称
     */
    @Id
    private String triggerGroup;

    /**
     * 字符串属性，占位用
     */
    private String strProp1;

    /**
     * 字符串属性，占位用
     */
    private String strProp2;

    /**
     * 字符串属性，占位用
     */
    private String strProp3;

    /**
     * 字符串属性，占位用
     */
    private Integer intProp1;

    /**
     * 字符串属性，占位用
     */
    private Integer intProp2;

    /**
     * 字符串属性，占位用
     */
    private Long longProp1;

    /**
     * 字符串属性，占位用
     */
    private Long longProp2;

    /**
     * 字符串属性，占位用
     */
    private BigDecimal decProp1;

    /**
     * 字符串属性，占位用
     */
    private BigDecimal decProp2;

    /**
     * 字符串属性，占位用
     */
    private String boolProp1;

    /**
     * 字符串属性，占位用
     */
    private String boolProp2;
}
