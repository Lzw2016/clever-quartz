package org.clever.quartz.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.clever.quartz.dto.request.QrtzSchedulerLogQueryRes;
import org.clever.quartz.entity.QrtzSchedulerLog;
import org.clever.quartz.mapper.QrtzSchedulerLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/6/1 11:31 <br/>
 */
@Service
@Transactional(readOnly = true)
public class SchedulerLogService {

    @Autowired
    private QrtzSchedulerLogMapper qrtzSchedulerLogMapper;

    @Transactional
    public QrtzSchedulerLog saveQrtzSchedulerLog(QrtzSchedulerLog qrtzSchedulerLog) {
        qrtzSchedulerLogMapper.insertSelective(qrtzSchedulerLog);
        return qrtzSchedulerLog;
    }

    /**
     * 分页查询调度器日志
     *
     * @return 触发器日志分页数据
     */
    public PageInfo<QrtzSchedulerLog> findByPage(QrtzSchedulerLogQueryRes qrtzSchedulerLogQueryRes) {
        return PageHelper
                .startPage(qrtzSchedulerLogQueryRes.getPageNo(), qrtzSchedulerLogQueryRes.getPageSize())
                .doSelectPageInfo(() -> qrtzSchedulerLogMapper.findByPage(qrtzSchedulerLogQueryRes));
    }
}
