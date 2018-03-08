package org.clever.quartz.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.clever.quartz.dto.request.JobLogQueryRes;
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
        qrtzJobLogMapper.insertSelective(qrtzJobLog);
        return qrtzJobLog;
    }

    @Transactional
    public boolean updateQrtzJobLog(QrtzJobLog qrtzJobLog) {
        int count = qrtzJobLogMapper.updateByPrimaryKeySelective(qrtzJobLog);
        return count > 0;
    }

    /**
     * 分页查询所有的定时任务日志
     *
     * @return 触发器日志分页数据
     */
    public PageInfo<QrtzJobLog> findByPage(JobLogQueryRes jobLogQueryRes) {
        return PageHelper.startPage(jobLogQueryRes.getPageNo(), jobLogQueryRes.getPageSize()).doSelectPageInfo(() -> qrtzJobLogMapper.findByPage(jobLogQueryRes));
    }
}