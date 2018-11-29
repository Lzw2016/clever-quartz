package org.clever.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clever.quartz.dto.response.TriggerKeyRes;
import org.clever.quartz.dto.response.TriggersRes;
import org.clever.quartz.entity.QrtzTriggers;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/6/1 11:56 <br/>
 */
@Repository
@Mapper
public interface QrtzTriggersMapper extends BaseMapper<QrtzTriggers> {

    List<TriggersRes> findTriggers(
            @Param("schedName") String schedName,
            @Param("triggerGroup") String triggerGroup,
            @Param("triggerName") String triggerName,
            @Param("jobGroup") String jobGroup,
            @Param("jobName") String jobName,
            IPage page
    );

    List<TriggersRes> getSimpleTriggerByJobKey(@Param("schedName") String schedName, @Param("jobName") String jobName, @Param("jobGroup") String jobGroup);

    List<TriggersRes> getCronTriggerByJobKey(@Param("schedName") String schedName, @Param("jobName") String jobName, @Param("jobGroup") String jobGroup);

    List<TriggersRes> getBlobTriggersByJobKey(@Param("schedName") String schedName, @Param("jobName") String jobName, @Param("jobGroup") String jobGroup);

    List<QrtzTriggers> getByJobKey(@Param("schedName") String schedName, @Param("jobName") String jobName, @Param("jobGroup") String jobGroup);

    List<TriggerKeyRes> getTriggerKeyByGroup(@Param("schedName") String schedName, @Param("triggerGroup") String triggerGroup);
}
