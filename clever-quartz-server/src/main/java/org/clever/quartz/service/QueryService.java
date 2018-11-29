package org.clever.quartz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.exception.BusinessException;
import org.clever.quartz.dto.request.FindJobDetailReq;
import org.clever.quartz.dto.response.JobAndTriggerRes;
import org.clever.quartz.dto.response.TriggersRes;
import org.clever.quartz.mapper.QrtzTriggersMapper;
import org.clever.quartz.mapper.QueryMapper;
import org.clever.quartz.utils.QuartzManager;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-29 21:21 <br/>
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class QueryService {

    @Autowired
    private QueryMapper queryMapper;
    @Autowired
    private QrtzTriggersMapper qrtzTriggersMapper;

    public IPage<JobAndTriggerRes> queryJobDetailsAndTriggers(FindJobDetailReq req) {
        Scheduler scheduler = QuartzManager.getScheduler();
        Page<JobAndTriggerRes> page = new Page<>(req.getPageNo(), req.getPageSize());
        try {
            page.setRecords(queryMapper.find(scheduler.getSchedulerName(), req.getJobName(), req.getJobGroup(), req.getJobClassName(), page));
            for (JobAndTriggerRes jobAndTriggerRes : page.getRecords()) {
                List<TriggersRes> triggersResList = qrtzTriggersMapper.findTriggers(
                        jobAndTriggerRes.getSchedName(),
                        null,
                        null,
                        jobAndTriggerRes.getJobGroup(),
                        jobAndTriggerRes.getJobName(),
                        null
                );
                jobAndTriggerRes.setTriggersResList(triggersResList);
            }
        } catch (Throwable e) {
            throw new BusinessException("查询JobDetail失败", e);
        }
        return page;
    }
}
