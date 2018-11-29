package org.clever.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.clever.quartz.entity.QrtzSchedulerState;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:42 <br/>
 */
@Repository
@Mapper
public interface QrtzSchedulerStateMapper extends BaseMapper<QrtzSchedulerState> {

    @Select("SELECT * FROM QRTZ_SCHEDULER_STATE WHERE SCHED_NAME=#{schedName}")
    List<QrtzSchedulerState> findBySchedName(@Param("schedName") String schedName);
}
