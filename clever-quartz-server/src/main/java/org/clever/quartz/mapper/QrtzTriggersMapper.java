package org.clever.quartz.mapper;

import org.apache.ibatis.annotations.Param;
import org.clever.quartz.dto.response.TriggersRes;
import org.clever.quartz.entity.QrtzTriggers;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/6/1 11:56 <br/>
 */
public interface QrtzTriggersMapper extends tk.mybatis.mapper.common.Mapper<QrtzTriggers> {

    List<TriggersRes> getSimpleTriggerByJobKey(@Param("schedName") String schedName, @Param("jobName") String jobName, @Param("jobGroup") String jobGroup);

    List<TriggersRes> getCronTriggerByJobKey(@Param("schedName") String schedName, @Param("jobName") String jobName, @Param("jobGroup") String jobGroup);

    List<TriggersRes> getBlobTriggersByJobKey(@Param("schedName") String schedName, @Param("jobName") String jobName, @Param("jobGroup") String jobGroup);

    List<QrtzTriggers> getByJobKey(@Param("schedName") String schedName, @Param("jobName") String jobName, @Param("jobGroup") String jobGroup);


}
