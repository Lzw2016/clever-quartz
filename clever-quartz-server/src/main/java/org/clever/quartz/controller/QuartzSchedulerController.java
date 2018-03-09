package org.clever.quartz.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.model.response.AjaxMessage;
import org.clever.common.server.controller.BaseController;
import org.clever.quartz.dto.request.JobDetailKeyReq;
import org.clever.quartz.model.QuartzJobDetails;
import org.clever.quartz.service.QuartzSchedulerService;
import org.quartz.JobKey;
import org.quartz.SchedulerMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/6/1 13:55 <br/>
 */
@Api(description = "Scheduler管理")
@RequestMapping(value = "/api/quartz")
@RestController
public class QuartzSchedulerController extends BaseController {

    @Autowired
    private QuartzSchedulerService schedulerService;

    @ApiOperation("暂停调度器")
    @PostMapping("/scheduler/standby" + JSON_SUFFIX)
    public AjaxMessage<String> standby() {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "暂停调度器成功", null);
        schedulerService.standby(ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("继续运行调度器")
    @PostMapping("/scheduler/start" + JSON_SUFFIX)
    public AjaxMessage<String> start() {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "继续运行调度器成功", null);
        schedulerService.start(ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("暂停所有的触发器")
    @PostMapping("/scheduler/pause_all" + JSON_SUFFIX)
    public AjaxMessage<String> pauseAll() {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "暂停所有的触发器成功", null);
        schedulerService.pauseAll(ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("取消暂停所有的触发器")
    @PostMapping("/scheduler/resume_all" + JSON_SUFFIX)
    public AjaxMessage<String> resumeAll() {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "取消暂停所有的触发器成功", null);
        schedulerService.resumeAll(ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("获取正在运行的Job成功")
    @GetMapping("/scheduler/running_job" + JSON_SUFFIX)
    @ResponseBody
    public AjaxMessage<List<QuartzJobDetails>> getRunningJobs() {
        AjaxMessage<List<QuartzJobDetails>> ajaxMessage = new AjaxMessage<>(true, "获取正在运行的Job成功", null);
        ajaxMessage.setResult(schedulerService.getRunningJobs(ajaxMessage));
        return ajaxMessage;
    }

    @ApiOperation("中断Job")
    @PostMapping("/scheduler/interrupt" + JSON_SUFFIX)
    public AjaxMessage<String> interrupt(@Validated @RequestBody JobDetailKeyReq jobDetailKeyReq) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "中断Job成功", null);
        schedulerService.interrupt(JobKey.jobKey(jobDetailKeyReq.getJobName(), jobDetailKeyReq.getJobGroup()), ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("获取Scheduler信息")
    @GetMapping("/scheduler/meta_data" + JSON_SUFFIX)
    public AjaxMessage<SchedulerMetaData> getMetaData() {
        AjaxMessage<SchedulerMetaData> ajaxMessage = new AjaxMessage<>(true, "获取Scheduler信息成功", null);
        ajaxMessage.setResult(schedulerService.getMetaData(ajaxMessage));
        return ajaxMessage;
    }

    @ApiOperation("获取SchedulerContext")
    @GetMapping("/scheduler/context" + JSON_SUFFIX)
    public AjaxMessage<Map<String, Object>> getContext() {
        AjaxMessage<Map<String, Object>> ajaxMessage = new AjaxMessage<>(true, "获取SchedulerContext成功", null);
        ajaxMessage.setResult(schedulerService.getContext(ajaxMessage));
        return ajaxMessage;
    }
}
