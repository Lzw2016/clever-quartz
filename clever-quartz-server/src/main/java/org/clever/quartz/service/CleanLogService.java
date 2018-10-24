package org.clever.quartz.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.DateTimeUtils;
import org.clever.quartz.mapper.QrtzJobLogMapper;
import org.clever.quartz.mapper.QrtzSchedulerLogMapper;
import org.clever.quartz.mapper.QrtzTriggerLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-31 13:23 <br/>
 */
@Service
@Slf4j
public class CleanLogService {

    @Autowired
    private QrtzSchedulerLogMapper qrtzSchedulerLogMapper;
    @Autowired
    private QrtzTriggerLogMapper qrtzTriggerLogMapper;
    @Autowired
    private QrtzJobLogMapper qrtzJobLogMapper;

    @Transactional
    public void cleanLog(Date date) {
        int count = qrtzSchedulerLogMapper.deleteByTime(date);
        log.info("清除日志[qrtz_scheduler_log]，数据量{}条， 时间<={}", count, DateTimeUtils.formatToString(date));
        count = qrtzTriggerLogMapper.deleteByTime(date);
        log.info("清除日志[qrtz_trigger_log]，数据量{}条， 时间<={}", count, DateTimeUtils.formatToString(date));
        count = qrtzJobLogMapper.deleteByTime(date);
        log.info("清除日志[qrtz_job_log]，数据量{}条， 时间<={}", count, DateTimeUtils.formatToString(date));
    }
}
