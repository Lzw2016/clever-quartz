package org.clever.quartz.mapper;

import org.apache.ibatis.annotations.Param;
import org.clever.quartz.dto.response.JobKeyRes;
import org.clever.quartz.entity.QrtzJobDetails;
import org.quartz.JobKey;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:41 <br/>
 */
public interface QrtzJobDetailsMapper extends Mapper<QrtzJobDetails> {

    List<QrtzJobDetails> find(
            @Param("schedName") String schedName,
            @Param("jobName") String jobName,
            @Param("jobGroup") String jobGroup,
            @Param("jobClassName") String jobClassName);

    List<JobKeyRes> getJobKeyByGroup(@Param("schedName") String schedName, @Param("jobGroup") String jobGroup);
}
