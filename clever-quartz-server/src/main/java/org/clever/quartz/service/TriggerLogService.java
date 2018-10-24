package org.clever.quartz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.clever.quartz.dto.request.TriggerLogQueryReq;
import org.clever.quartz.entity.QrtzTriggerLog;
import org.clever.quartz.mapper.QrtzTriggerLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：LiZW <br/>
 * 创建时间：2016-8-3 23:35 <br/>
 */
@Service
@Transactional(readOnly = true)
public class TriggerLogService {

    @Autowired
    private QrtzTriggerLogMapper qrtzTriggerLogMapper;

    @Transactional
    public QrtzTriggerLog addQrtzTriggerLog(QrtzTriggerLog qrtzTriggerLog) {
        qrtzTriggerLogMapper.insert(qrtzTriggerLog);
        return qrtzTriggerLog;
    }

    @Transactional
    public QrtzTriggerLog updateQrtzTriggerLog(QrtzTriggerLog qrtzTriggerLog) {
        qrtzTriggerLogMapper.updateById(qrtzTriggerLog);
        qrtzTriggerLog = qrtzTriggerLogMapper.selectById(qrtzTriggerLog.getId());
        return qrtzTriggerLog;
    }

    /**
     * 分页查询所有的触发器日志
     *
     * @return 触发器日志分页数据
     */
    public IPage<QrtzTriggerLog> findByPage(TriggerLogQueryReq triggerLogQueryReq) {
        Page<QrtzTriggerLog> page = new Page<>(triggerLogQueryReq.getPageNo(), triggerLogQueryReq.getPageSize());
        page.setRecords(qrtzTriggerLogMapper.findByPage(triggerLogQueryReq, page));
        return page;
    }
}

