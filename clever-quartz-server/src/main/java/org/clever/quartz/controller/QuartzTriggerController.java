package org.clever.quartz.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.model.response.AjaxMessage;
import org.clever.common.server.controller.BaseController;
import org.clever.quartz.dto.request.*;
import org.clever.quartz.model.QuartzTriggers;
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
    public AjaxMessage<List<String>> validatorCron(@Validated ValidatorCronRes validatorCronRes) {
        AjaxMessage<List<String>> ajaxMessage = new AjaxMessage<>(true, "cron表达式验证成功", null);
        List<String> dateList = triggerService.validatorCron(validatorCronRes.getCron(), validatorCronRes.getNum(), ajaxMessage);
        ajaxMessage.setResult(dateList);
        return ajaxMessage;
    }

    @ApiOperation("给JobDetail增加一个SimpleTrigger")
    @PostMapping("/trigger/add_simple_trigger_for_job" + JSON_SUFFIX)
    public AjaxMessage<String> addSimpleTriggerForJob(@Validated @RequestBody AddSimpleTriggerForJobRes addSimpleTriggerForJobRes) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "给JobDetail增加一个SimpleTrigger成功", null);
        triggerService.addSimpleTriggerForJob(addSimpleTriggerForJobRes, ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("给JobDetail增加一个CronTrigger")
    @PostMapping("/trigger/add_cron_trigger_for_job" + JSON_SUFFIX)
    public AjaxMessage<String> addCronTriggerForJob(@Validated @RequestBody AddCronTriggerForJobRes addCronTriggerForJobRes) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "给JobDetail增加一个CronTrigger成功", null);
        triggerService.addCronTriggerForJob(addCronTriggerForJobRes, ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("获取JobDetail的所有Trigger")
    @GetMapping("/trigger/job" + JSON_SUFFIX)
    public AjaxMessage<List<QuartzTriggers>> getTriggerByJob(@Validated JobDetailKeyRes jobDetailKeyRes) {
        AjaxMessage<List<QuartzTriggers>> ajaxMessage = new AjaxMessage<>(true, "获取JobDetail的所有Trigger成功", null);
        List<QuartzTriggers> qrtzTriggersList = triggerService.getTriggerByJob(jobDetailKeyRes, ajaxMessage);
        ajaxMessage.setResult(qrtzTriggersList);
        return ajaxMessage;
    }

    @ApiOperation("获取所有的TriggerGroupName")
    @GetMapping("/trigger/all_trigger_group_name" + JSON_SUFFIX)
    AjaxMessage<List<String>> getTriggerGroupNames() {
        AjaxMessage<List<String>> ajaxMessage = new AjaxMessage<>(true, "获取所有的TriggerGroupName成功", null);
        ajaxMessage.setResult(triggerService.getTriggerGroupNames(ajaxMessage));
        return ajaxMessage;
    }

    @ApiOperation("删除一个JobDetail的所有Trigger")
    @DeleteMapping("/trigger/job" + JSON_SUFFIX)
    public AjaxMessage<String> deleteTriggerByJob(@Validated JobDetailKeyRes jobDetailKeyRes) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "删除一个JobDetail的所有Trigger成功", null);
        triggerService.deleteTriggerByJob(jobDetailKeyRes, ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("暂停而且删除Trigger")
    @DeleteMapping("/trigger" + JSON_SUFFIX)
    public AjaxMessage<String> deleteTrigger(@Validated TriggerKeyRes triggerKeyRes) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "删除Trigger成功", null);
        triggerService.deleteTrigger(triggerKeyRes, ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("暂停Trigger")
    @PostMapping("/trigger/pause" + JSON_SUFFIX)
    public AjaxMessage<String> pauseTrigger(@Validated @RequestBody TriggerKeyRes triggerKeyRes) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "暂停Trigger成功", null);
        triggerService.pauseTrigger(triggerKeyRes, ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("取消暂停Trigger")
    @PostMapping("/trigger/resume" + JSON_SUFFIX)
    public AjaxMessage<String> resumeTrigger(@Validated @RequestBody TriggerKeyRes triggerKeyRes) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "取消暂停Trigger成功", null);
        triggerService.resumeTrigger(triggerKeyRes, ajaxMessage);
        return ajaxMessage;
    }
}
