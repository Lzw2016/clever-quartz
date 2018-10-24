package org.clever.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.clever.quartz.dto.response.TriggerKeyRes;
import org.clever.quartz.dto.response.TriggersRes;
import org.clever.quartz.entity.QrtzTriggers;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/6/1 11:56 <br/>
 */
public interface QrtzTriggersMapper extends BaseMapper<QrtzTriggers> {

    List<TriggersRes> getSimpleTriggerByJobKey(@Param("schedulerName") String schedulerName, @Param("jobName") String jobName, @Param("jobGroup") String jobGroup);

    List<TriggersRes> getCronTriggerByJobKey(@Param("schedulerName") String schedulerName, @Param("jobName") String jobName, @Param("jobGroup") String jobGroup);

    List<TriggersRes> getBlobTriggersByJobKey(@Param("schedulerName") String schedulerName, @Param("jobName") String jobName, @Param("jobGroup") String jobGroup);

    List<QrtzTriggers> getByJobKey(@Param("schedulerName") String schedulerName, @Param("jobName") String jobName, @Param("jobGroup") String jobGroup);

    List<TriggerKeyRes> getTriggerKeyByGroup(@Param("schedulerName") String schedulerName, @Param("triggerGroup") String triggerGroup);
}
