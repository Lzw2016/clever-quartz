package org.clever.quartz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.clever.quartz.dto.request.JobLogQueryReq;
import org.clever.quartz.entity.QrtzJobLog;
import org.clever.quartz.mapper.QrtzJobLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：LiZW <br/>
 * 创建时间：2016-8-3 23:34 <br/>
 */
@Service
@Transactional(readOnly = true)
public class JobLogService {

    @Autowired
    private QrtzJobLogMapper qrtzJobLogMapper;

    @Transactional
    public QrtzJobLog addQrtzJobLog(QrtzJobLog qrtzJobLog) {
        qrtzJobLogMapper.insert(qrtzJobLog);
        return qrtzJobLog;
    }

    @Transactional
    public boolean updateQrtzJobLog(QrtzJobLog qrtzJobLog) {
        int count = qrtzJobLogMapper.updateById(qrtzJobLog);
        return count > 0;
    }

    /**
     * 分页查询所有的定时任务日志
     *
     * @return 触发器日志分页数据
     */
    public IPage<QrtzJobLog> findByPage(JobLogQueryReq jobLogQueryReq) {
        Page<QrtzJobLog> page = new Page<>(jobLogQueryReq.getPageNo(), jobLogQueryReq.getPageSize());
        page.setRecords(qrtzJobLogMapper.findByPage(jobLogQueryReq, page));
        return page;
    }
}