package org.clever.quartz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.clever.quartz.dto.request.QrtzSchedulerLogQueryReq;
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
        qrtzSchedulerLogMapper.insert(qrtzSchedulerLog);
        return qrtzSchedulerLog;
    }

    /**
     * 分页查询调度器日志
     *
     * @return 触发器日志分页数据
     */
    public IPage<QrtzSchedulerLog> findByPage(QrtzSchedulerLogQueryReq qrtzSchedulerLogQueryReq) {
        Page<QrtzSchedulerLog> page = new Page<>(qrtzSchedulerLogQueryReq.getPageNo(), qrtzSchedulerLogQueryReq.getPageSize());
        page.setRecords(qrtzSchedulerLogMapper.findByPage(qrtzSchedulerLogQueryReq, page));
        return page;
    }
}
