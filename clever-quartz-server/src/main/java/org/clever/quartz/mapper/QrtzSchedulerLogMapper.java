package org.clever.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clever.quartz.dto.request.QrtzSchedulerLogQueryReq;
import org.clever.quartz.entity.QrtzSchedulerLog;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/6/1 11:38 <br/>
 */
@Repository
@Mapper
public interface QrtzSchedulerLogMapper extends BaseMapper<QrtzSchedulerLog> {

    /**
     * 查询调度器日志
     *
     * @return 触发器日志分页数据
     */
    List<QrtzSchedulerLog> findByPage(@Param("query") QrtzSchedulerLogQueryReq qrtzSchedulerLogQueryReq, IPage page);

    /**
     * 删除 date 之前创建的日志数据
     */
    int deleteByTime(@Param("date") Date date);
}
