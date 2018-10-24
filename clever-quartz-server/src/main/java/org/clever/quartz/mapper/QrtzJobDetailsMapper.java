package org.clever.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.clever.quartz.dto.response.JobDetailsRes;
import org.clever.quartz.dto.response.JobKeyRes;
import org.clever.quartz.entity.QrtzJobDetails;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-09 18:41 <br/>
 */
public interface QrtzJobDetailsMapper extends BaseMapper<QrtzJobDetails> {

    List<JobDetailsRes> find(
            @Param("schedulerName") String schedulerName,
            @Param("jobName") String jobName,
            @Param("jobGroup") String jobGroup,
            @Param("jobClassName") String jobClassName,
            IPage page);

    List<JobKeyRes> getJobKeyByGroup(@Param("schedulerName") String schedulerName, @Param("jobGroup") String jobGroup);
}
