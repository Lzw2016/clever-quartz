package org.clever.quartz.mapper;

import org.clever.quartz.dto.request.TriggerLogQueryRes;
import org.clever.quartz.entity.QrtzTriggerLog;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/6/1 11:42 <br/>
 */
public interface QrtzTriggerLogMapper extends tk.mybatis.mapper.common.Mapper<QrtzTriggerLog> {

    /**
     * 查询所有的触发器日志
     *
     * @return 触发器日志分页数据
     */
    List<QrtzTriggerLog> findByPage(TriggerLogQueryRes triggerLogQueryRes);
}
