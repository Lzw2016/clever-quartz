package org.clever.quartz.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.model.response.AjaxMessage;
import org.clever.common.server.controller.BaseController;
import org.clever.quartz.dto.request.JobDetailKeyRes;
import org.clever.quartz.dto.request.SaveJobDetailRes;
import org.clever.quartz.model.QuartzJobDetails;
import org.clever.quartz.service.QuartzJobDetailService;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/6/1 10:50 <br/>
 */
@Api(description = "Job管理")
@RequestMapping(value = "/api/quartz")
@RestController
public class QuartzJobDetailController extends BaseController {

    @Autowired
    private QuartzJobDetailService jobDetailService;

    @ApiOperation("获取所有的Job Key")
    @GetMapping("/job_detail/all_job_key" + JSON_SUFFIX)
    public AjaxMessage<List<JobKey>> getAllJobKey() {
        AjaxMessage<List<JobKey>> ajaxMessage = new AjaxMessage<>(true, "获取所有的JobKey成功", null);
        List<JobKey> jobKeyList = jobDetailService.getAllJobKey(ajaxMessage);
        ajaxMessage.setResult(jobKeyList);
        return ajaxMessage;
    }

    // TODO 条件查询Job
    @ApiOperation("获取所有的Job信息")
    @GetMapping("/job_detail/all_job_detail" + JSON_SUFFIX)
    public AjaxMessage<List<QuartzJobDetails>> getAllJobDetail() {
        AjaxMessage<List<QuartzJobDetails>> ajaxMessage = new AjaxMessage<>(true, "获取所有的JobDetail成功", null);
        List<QuartzJobDetails> jobDetailList = jobDetailService.getAllJobDetail();
        ajaxMessage.setResult(jobDetailList);
        return ajaxMessage;
    }

    @ApiOperation("获取所有的Job类型")
    @GetMapping("/job_detail/all_job_classname" + JSON_SUFFIX)
    public AjaxMessage<List<String>> getAllJobClassName() {
        AjaxMessage<List<String>> ajaxMessage = new AjaxMessage<>(true, "获取所有的Job类名成功", null);
        List<String> jobClassNameList = jobDetailService.getAllJobClassName();
        ajaxMessage.setResult(jobClassNameList);
        return ajaxMessage;
    }

    @ApiOperation("获取所有的Job Group Name")
    @GetMapping("/job_detail/all_job_group_name" + JSON_SUFFIX)
    public AjaxMessage<List<String>> getJobGroupNames() {
        AjaxMessage<List<String>> ajaxMessage = new AjaxMessage<>(true, "获取所有的JobGroupName成功", null);
        List<String> jobGroupNames = jobDetailService.getJobGroupNames(ajaxMessage);
        ajaxMessage.setResult(jobGroupNames);
        return ajaxMessage;
    }

    @ApiOperation("保存Job")
    @PostMapping("/job_detail" + JSON_SUFFIX)
    public AjaxMessage<String> saveJobDetail(@Validated @RequestBody SaveJobDetailRes saveJobDetailRes) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "保存JobDetail成功", null);
        jobDetailService.saveJobDetail(saveJobDetailRes, ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("删除Job")
    @DeleteMapping("/job_detail" + JSON_SUFFIX)
    public AjaxMessage<String> deleteJobDetail(@Validated JobDetailKeyRes jobDetailKeyRes) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "删除JobDetail成功", null);
        jobDetailService.deleteJobDetail(jobDetailKeyRes, ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("暂停Job")
    @PostMapping("/job_detail/pause" + JSON_SUFFIX)
    public AjaxMessage<String> pauseJob(@Validated @RequestBody JobDetailKeyRes jobDetailKeyRes) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "暂停JobDetail成功", null);
        jobDetailService.pauseJob(jobDetailKeyRes, ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("取消暂停Job")
    @PostMapping("/job_detail/resume" + JSON_SUFFIX)
    public AjaxMessage<String> resumeJob(@Validated @RequestBody JobDetailKeyRes jobDetailKeyRes) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "取消暂停JobDetail成功", null);
        jobDetailService.resumeJob(jobDetailKeyRes, ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("立即运行Job")
    @PostMapping("/job_detail/trigger" + JSON_SUFFIX)
    public AjaxMessage<String> triggerJob(@Validated @RequestBody JobDetailKeyRes jobDetailKeyRes) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "立即运行JobDetail成功", null);
        jobDetailService.triggerJob(jobDetailKeyRes, ajaxMessage);
        return ajaxMessage;
    }
}
