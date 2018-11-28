package org.clever.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clever.quartz.dto.request.JobLogQueryReq;
import org.clever.quartz.entity.QrtzJobLog;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/6/1 10:00 <br/>
 */
@Repository
@Mapper
public interface QrtzJobLogMapper extends BaseMapper<QrtzJobLog> {

    /**
     * 查询所有的定时任务日志
     *
     * @return 触发器日志分页数据
     */
    List<QrtzJobLog> findByPage(@Param("query") JobLogQueryReq jobLogQueryReq, IPage page);

    /**
     * 删除 date 之前创建的日志数据
     */
    int deleteByTime(@Param("date") Date date);
}
