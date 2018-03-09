package org.clever.quartz.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.server.controller.BaseController;
import org.clever.quartz.dto.request.JobLogQueryRes;
import org.clever.quartz.dto.request.QrtzSchedulerLogQueryRes;
import org.clever.quartz.dto.request.TriggerLogQueryRes;
import org.clever.quartz.entity.QrtzJobLog;
import org.clever.quartz.entity.QrtzSchedulerLog;
import org.clever.quartz.entity.QrtzTriggerLog;
import org.clever.quartz.service.JobLogService;
import org.clever.quartz.service.SchedulerLogService;
import org.clever.quartz.service.TriggerLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/6/2 10:53 <br/>
 */
@Api(description = "日志查询")
@RequestMapping(value = "/api/quartz")
@RestController
public class LogController extends BaseController {

    @Autowired
    private JobLogService qrtzJobLogService;
    @Autowired
    private TriggerLogService qrtzTriggerLogService;
    @Autowired
    private SchedulerLogService schedulerLogService;

    @ApiOperation("查询Job执行日志")
    @GetMapping("/log/job_log" + JSON_SUFFIX)
    public PageInfo<QrtzJobLog> findJobLogByPage(@Validated JobLogQueryRes jobLogQueryRes) {
        return qrtzJobLogService.findByPage(jobLogQueryRes);
    }

    @ApiOperation("查询trigger触发日志")
    @GetMapping("/log/trigger_log" + JSON_SUFFIX)
    public PageInfo<QrtzTriggerLog> findTriggerLogByPage(@Validated TriggerLogQueryRes triggerLogQueryRes) {
        return qrtzTriggerLogService.findByPage(triggerLogQueryRes);
    }

    @ApiOperation("查询scheduler日志")
    @GetMapping("/log/scheduler_log" + JSON_SUFFIX)
    public PageInfo<QrtzSchedulerLog> findSchedulerLogByPage(@Validated QrtzSchedulerLogQueryRes qrtzSchedulerLogQueryRes) {
        return schedulerLogService.findByPage(qrtzSchedulerLogQueryRes);
    }
}
