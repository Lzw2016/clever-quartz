package org.clever.quartz.mapper;

import org.apache.ibatis.annotations.Param;
import org.clever.quartz.dto.request.TriggerLogQueryReq;
import org.clever.quartz.entity.QrtzTriggerLog;

import java.util.Date;
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
    List<QrtzTriggerLog> findByPage(TriggerLogQueryReq triggerLogQueryReq);

    /**
     * 删除 date 之前创建的日志数据
     */
    int deleteByTime(@Param("date") Date date);
}
