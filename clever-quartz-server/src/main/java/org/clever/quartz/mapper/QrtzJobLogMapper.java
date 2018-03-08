package org.clever.quartz.mapper;

import org.clever.quartz.dto.request.JobLogQueryRes;
import org.clever.quartz.entity.QrtzJobLog;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/6/1 10:00 <br/>
 */
public interface QrtzJobLogMapper extends tk.mybatis.mapper.common.Mapper<QrtzJobLog> {

    /**
     * 查询所有的定时任务日志
     *
     * @return 触发器日志分页数据
     */
    List<QrtzJobLog> findByPage(JobLogQueryRes jobLogQueryRes);
}
