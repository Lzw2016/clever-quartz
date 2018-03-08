package org.clever.quartz.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.model.response.AjaxMessage;
import org.clever.common.server.controller.BaseController;
import org.clever.quartz.dto.request.JobDetailKeyVo;
import org.clever.quartz.dto.request.SaveJobDetailVo;
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
@RequestMapping(value = "/api/quartz/job_detail")
@RestController
public class QuartzJobDetailController extends BaseController {

    @Autowired
    private QuartzJobDetailService jobDetailService;

    @ApiOperation("获取所有的Job Key")
    @GetMapping("/all_job_key" + JSON_SUFFIX)
    public AjaxMessage<List<JobKey>> getAllJobKey() {
        AjaxMessage<List<JobKey>> ajaxMessage = new AjaxMessage<>(true, "获取所有的JobKey成功", null);
        List<JobKey> jobKeyList = jobDetailService.getAllJobKey(ajaxMessage);
        ajaxMessage.setResult(jobKeyList);
        return ajaxMessage;
    }

    // TODO 条件查询Job
    @ApiOperation("获取所有的Job信息")
    @GetMapping("/all_job_detail" + JSON_SUFFIX)
    public AjaxMessage<List<QuartzJobDetails>> getAllJobDetail() {
        AjaxMessage<List<QuartzJobDetails>> ajaxMessage = new AjaxMessage<>(true, "获取所有的JobDetail成功", null);
        List<QuartzJobDetails> jobDetailList = jobDetailService.getAllJobDetail();
        ajaxMessage.setResult(jobDetailList);
        return ajaxMessage;
    }

    @ApiOperation("获取所有的Job类型")
    @GetMapping("/all_job_classname" + JSON_SUFFIX)
    public AjaxMessage<List<String>> getAllJobClassName() {
        AjaxMessage<List<String>> ajaxMessage = new AjaxMessage<>(true, "获取所有的Job类名成功", null);
        List<String> jobClassNameList = jobDetailService.getAllJobClassName();
        ajaxMessage.setResult(jobClassNameList);
        return ajaxMessage;
    }

    @ApiOperation("获取所有的Job Group Name")
    @GetMapping("/all_job_group_name" + JSON_SUFFIX)
    public AjaxMessage<List<String>> getJobGroupNames() {
        AjaxMessage<List<String>> ajaxMessage = new AjaxMessage<>(true, "获取所有的JobGroupName成功", null);
        List<String> jobGroupNames = jobDetailService.getJobGroupNames(ajaxMessage);
        ajaxMessage.setResult(jobGroupNames);
        return ajaxMessage;
    }

    @ApiOperation("保存Job")
    @PostMapping("/" + JSON_SUFFIX)
    public AjaxMessage<String> saveJobDetail(@Validated @RequestBody SaveJobDetailVo saveJobDetailVo) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "保存JobDetail成功", null);
        jobDetailService.saveJobDetail(saveJobDetailVo, ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("删除Job")
    @DeleteMapping("/" + JSON_SUFFIX)
    public AjaxMessage<String> deleteJobDetail(@Validated JobDetailKeyVo jobDetailKeyVo) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "删除JobDetail成功", null);
        jobDetailService.deleteJobDetail(jobDetailKeyVo, ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("暂停Job")
    @PostMapping("/pause" + JSON_SUFFIX)
    public AjaxMessage<String> pauseJob(@Validated @RequestBody JobDetailKeyVo jobDetailKeyVo) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "暂停JobDetail成功", null);
        jobDetailService.pauseJob(jobDetailKeyVo, ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("取消暂停Job")
    @PostMapping("/resume" + JSON_SUFFIX)
    public AjaxMessage<String> resumeJob(@Validated @RequestBody JobDetailKeyVo jobDetailKeyVo) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "取消暂停JobDetail成功", null);
        jobDetailService.resumeJob(jobDetailKeyVo, ajaxMessage);
        return ajaxMessage;
    }

    @ApiOperation("立即运行Job")
    @PostMapping("/trigger" + JSON_SUFFIX)
    public AjaxMessage<String> triggerJob(@Validated @RequestBody JobDetailKeyVo jobDetailKeyVo) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "立即运行JobDetail成功", null);
        jobDetailService.triggerJob(jobDetailKeyVo, ajaxMessage);
        return ajaxMessage;
    }
}
