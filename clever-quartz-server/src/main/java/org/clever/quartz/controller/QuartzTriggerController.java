package org.clever.quartz.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.model.response.AjaxMessage;
import org.clever.common.server.controller.BaseController;
import org.clever.quartz.dto.request.*;
import org.clever.quartz.dto.response.TriggersRes;
import org.clever.quartz.service.QuartzTriggerService;
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
    public AjaxMessage<List<String>> validatorCron(@Validated ValidatorCronReq validatorCronReq) {
        AjaxMessage<List<String>> ajaxMessage = new AjaxMessage<>(true, "cron表达式验证成功", null);
        List<String> dateList = triggerService.validatorCron(validatorCronReq.getCron(), validatorCronReq.getNum(), ajaxMessage);
        ajaxMessage.setResult(dateList);
        return ajaxMessage;
    }

    @ApiOperation("给JobDetail增加一个SimpleTrigger")
    @PostMapping("/trigger/add_simple_trigger_for_job" + JSON_SUFFIX)
    public AjaxMessage<String> addSimpleTriggerForJob(@Validated @RequestBody AddSimpleTriggerForJobReq addSimpleTriggerForJobReq) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "给JobDetail增加一个SimpleTrigger成功", null);
        triggerService.addSimpleTriggerForJob(addSimpleTriggerForJobReq, ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("给JobDetail增加一个CronTrigger")
    @PostMapping("/trigger/add_cron_trigger_for_job" + JSON_SUFFIX)
    public AjaxMessage<String> addCronTriggerForJob(@Validated @RequestBody AddCronTriggerForJobReq addCronTriggerForJobReq) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "给JobDetail增加一个CronTrigger成功", null);
        triggerService.addCronTriggerForJob(addCronTriggerForJobReq, ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("删除一个JobDetail的所有Trigger")
    @DeleteMapping("/trigger/job" + JSON_SUFFIX)
    public AjaxMessage<String> deleteTriggerByJob(@Validated JobDetailKeyReq jobDetailKeyReq) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "删除一个JobDetail的所有Trigger成功", null);
        triggerService.deleteTriggerByJob(jobDetailKeyReq);
        return ajaxMessage;
    }

    @ApiOperation("暂停而且删除Trigger")
    @DeleteMapping("/trigger" + JSON_SUFFIX)
    public AjaxMessage<String> deleteTrigger(@Validated TriggerKeyReq triggerKeyReq) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "删除Trigger成功", null);
        triggerService.deleteTrigger(triggerKeyReq, ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("暂停Trigger")
    @PostMapping("/trigger/pause" + JSON_SUFFIX)
    public AjaxMessage<String> pauseTrigger(@Validated @RequestBody TriggerKeyReq triggerKeyReq) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "暂停Trigger成功", null);
        triggerService.pauseTrigger(triggerKeyReq, ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("取消暂停Trigger")
    @PostMapping("/trigger/resume" + JSON_SUFFIX)
    public AjaxMessage<String> resumeTrigger(@Validated @RequestBody TriggerKeyReq triggerKeyReq) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "取消暂停Trigger成功", null);
        triggerService.resumeTrigger(triggerKeyReq, ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("获取JobDetail的所有Trigger")
    @GetMapping("/trigger/job" + JSON_SUFFIX)
    public AjaxMessage<List<TriggersRes>> getTriggerByJob(@Validated JobDetailKeyReq jobDetailKeyReq) {
        AjaxMessage<List<TriggersRes>> ajaxMessage = new AjaxMessage<>(true, "获取JobDetail的所有Trigger成功", null);
        List<TriggersRes> qrtzTriggersList = triggerService.getTriggerByJob(jobDetailKeyReq, ajaxMessage);
        ajaxMessage.setResult(qrtzTriggersList);
        return ajaxMessage;
    }

    @ApiOperation("获取所有的TriggerGroupName")
    @GetMapping("/trigger/all_trigger_group_name" + JSON_SUFFIX)
    public AjaxMessage<List<String>> getTriggerGroupNames() {
        AjaxMessage<List<String>> ajaxMessage = new AjaxMessage<>(true, "获取所有的TriggerGroupName成功", null);
        ajaxMessage.setResult(triggerService.getTriggerGroupNames(ajaxMessage));
        return ajaxMessage;
    }

    @ApiOperation("获取所有的CalendarName")
    @GetMapping("/trigger/all_calendar_names" + JSON_SUFFIX)
    public AjaxMessage<List<String>> getCalendarNames() {
        AjaxMessage<List<String>> ajaxMessage = new AjaxMessage<>(true, "获取所有的CalendarName成功", null);
        ajaxMessage.setResult(triggerService.getCalendarNames(ajaxMessage));
        return ajaxMessage;
    }
}
