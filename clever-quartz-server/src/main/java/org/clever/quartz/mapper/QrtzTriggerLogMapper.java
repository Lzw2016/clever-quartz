package org.clever.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clever.quartz.dto.request.TriggerLogQueryReq;
import org.clever.quartz.entity.QrtzTriggerLog;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/6/1 11:42 <br/>
 */
@Repository
@Mapper
public interface QrtzTriggerLogMapper extends BaseMapper<QrtzTriggerLog> {

    /**
     * 查询所有的触发器日志
     *
     * @return 触发器日志分页数据
     */
    List<QrtzTriggerLog> findByPage(@Param("query") TriggerLogQueryReq triggerLogQueryReq, IPage page);

    /**
     * 删除 date 之前创建的日志数据
     */
    int deleteByTime(@Param("date") Date date);
}
