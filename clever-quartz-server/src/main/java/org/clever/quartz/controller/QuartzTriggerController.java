package org.clever.quartz.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.model.response.AjaxMessage;
import org.clever.common.server.controller.BaseController;
import org.clever.quartz.dto.request.*;
import org.clever.quartz.dto.response.TriggerInfoRes;
import org.clever.quartz.dto.response.TriggerKeyRes;
import org.clever.quartz.dto.response.TriggersRes;
import org.clever.quartz.entity.QrtzTriggers;
import org.clever.quartz.service.QuartzTriggerService;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/6/1 18:03 <br/>
 */
@Api(description = "Trigger管理")
@RequestMapping(value = "/api/quartz")
@RestController
public class QuartzTriggerController extends BaseController {

    @Autowired
    private QuartzTriggerService triggerService;

    @ApiOperation("验证cron表达式")
    @GetMapping("/trigger/validator_cron" + JSON_SUFFIX)
    public List<String> validatorCron(@Validated ValidatorCronReq validatorCronReq) {
        return triggerService.validatorCron(validatorCronReq.getCron(), validatorCronReq.getNum());
    }

    @ApiOperation("给JobDetail增加一个SimpleTrigger")
    @PostMapping("/trigger/add_simple_trigger_for_job" + JSON_SUFFIX)
    public AddSimpleTriggerForJobReq addSimpleTriggerForJob(@Validated @RequestBody AddSimpleTriggerForJobReq addSimpleTriggerForJobReq) {
        triggerService.addSimpleTriggerForJob(addSimpleTriggerForJobReq);
        return addSimpleTriggerForJobReq;
    }

    @ApiOperation("给JobDetail增加一个CronTrigger")
    @PostMapping("/trigger/add_cron_trigger_for_job" + JSON_SUFFIX)
    public AddCronTriggerForJobReq addCronTriggerForJob(@Validated @RequestBody AddCronTriggerForJobReq addCronTriggerForJobReq) {
        triggerService.addCronTriggerForJob(addCronTriggerForJobReq);
        return addCronTriggerForJobReq;
    }

    @ApiOperation("删除一个JobDetail的所有Trigger")
    @DeleteMapping("/trigger/job" + JSON_SUFFIX)
    public List<QrtzTriggers> deleteTriggerByJob(@Validated JobDetailKeyReq jobDetailKeyReq) {
        return triggerService.deleteTriggerByJob(jobDetailKeyReq);
    }

    @ApiOperation("暂停而且删除Trigger")
    @DeleteMapping("/trigger" + JSON_SUFFIX)
    public Trigger deleteTrigger(@Validated TriggerKeyReq triggerKeyReq) {
        return triggerService.deleteTrigger(triggerKeyReq);
    }

    @ApiOperation("暂停Trigger")
    @PostMapping("/trigger/pause" + JSON_SUFFIX)
    public Trigger pauseTrigger(@Validated @RequestBody TriggerKeyReq triggerKeyReq) {
        return triggerService.pauseTrigger(triggerKeyReq);
    }

    @ApiOperation("取消暂停Trigger")
    @PostMapping("/trigger/resume" + JSON_SUFFIX)
    public Trigger resumeTrigger(@Validated @RequestBody TriggerKeyReq triggerKeyReq) {
        return triggerService.resumeTrigger(triggerKeyReq);
    }

    @ApiOperation("获取JobDetail的所有Trigger")
    @GetMapping("/trigger/job" + JSON_SUFFIX)
    public List<TriggersRes> getTriggerByJob(@Validated JobDetailKeyReq jobDetailKeyReq) {
        return triggerService.getTriggerByJob(jobDetailKeyReq);
    }

    @ApiOperation("获取Trigger")
    @GetMapping("/trigger/info/{triggerGroup}/{triggerName}" + JSON_SUFFIX)
    public TriggerInfoRes getTrigger(@PathVariable String triggerGroup, @PathVariable String triggerName) {
        return triggerService.getTrigger(triggerGroup, triggerName);
    }

    @ApiOperation("获取所有的TriggerGroupName")
    @GetMapping("/trigger/all_trigger_group_name" + JSON_SUFFIX)
    public List<String> getTriggerGroupNames() {
        return triggerService.getTriggerGroupNames();
    }

    @ApiOperation("获取所有的TriggerKey")
    @GetMapping("/trigger/trigger_group/{triggerGroup}" + JSON_SUFFIX)
    public List<TriggerKeyRes> getTriggerKeyByGroup(@PathVariable String triggerGroup) {
        return triggerService.getTriggerKeyByGroup(triggerGroup);
    }

    @ApiOperation("获取所有的CalendarName")
    @GetMapping("/trigger/all_calendar_names" + JSON_SUFFIX)
    public List<String> getCalendarNames() {
        return triggerService.getCalendarNames();
    }
}
