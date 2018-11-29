package org.clever.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clever.quartz.dto.response.JobAndTriggerRes;
import org.clever.quartz.entity.QrtzJobDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-29 21:29 <br/>
 */
@Repository
@Mapper
public interface QueryMapper extends BaseMapper<QrtzJobDetails> {

    List<JobAndTriggerRes> find(
            @Param("schedName") String schedName,
            @Param("jobName") String jobName,
            @Param("jobGroup") String jobGroup,
            @Param("jobClassName") String jobClassName,
            IPage page);
}
