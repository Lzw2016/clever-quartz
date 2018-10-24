package org.clever.quartz.mapper;

import org.apache.ibatis.annotations.Param;
import org.clever.quartz.dto.request.JobLogQueryReq;
import org.clever.quartz.entity.QrtzJobLog;

import java.util.Date;
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
    List<QrtzJobLog> findByPage(JobLogQueryReq jobLogQueryReq);

    /**
     * 删除 date 之前创建的日志数据
     */
    int deleteByTime(@Param("date") Date date);
}
