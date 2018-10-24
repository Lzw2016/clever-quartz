package org.clever.quartz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:33 <br/>
 */
@Data
public class QrtzSimpropTriggers implements Serializable {

    /**
     * Scheduler名称
     */
    @TableId
    private String schedulerName;

    /**
     * Trigger key
     */
    @TableId
    private String triggerName;

    /**
     * Trigger group 名称
     */
    @TableId
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
