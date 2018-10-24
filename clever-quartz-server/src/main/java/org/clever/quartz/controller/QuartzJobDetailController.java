package org.clever.quartz.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.server.controller.BaseController;
import org.clever.quartz.dto.request.FindJobDetailReq;
import org.clever.quartz.dto.request.JobDetailKeyReq;
import org.clever.quartz.dto.request.SaveJobDetailReq;
import org.clever.quartz.dto.response.JobDetailInfoRes;
import org.clever.quartz.dto.response.JobDetailsRes;
import org.clever.quartz.dto.response.JobKeyRes;
import org.clever.quartz.service.QuartzJobDetailService;
import org.quartz.JobDetail;
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

    @ApiOperation("获取所有的Job类型")
    @GetMapping("/job_detail/all_job_classname" + JSON_SUFFIX)
    public List<String> getAllJobClassName() {
        return jobDetailService.getAllJobClassName();
    }

    @ApiOperation("获取所有的Job Group Name")
    @GetMapping("/job_detail/all_job_group_name" + JSON_SUFFIX)
    public List<String> getJobGroupNames() {
        return jobDetailService.getJobGroupNames();
    }

    @ApiOperation("获取所有的Job Key")
    @GetMapping("/job_detail/job_group/{jobGroup}" + JSON_SUFFIX)
    public List<JobKeyRes> getJobKeyByGroup(@PathVariable String jobGroup) {
        return jobDetailService.getJobKeyByGroup(jobGroup);
    }

    @ApiOperation("查询Job信息")
    @GetMapping("/job_detail" + JSON_SUFFIX)
    public List<JobDetailsRes> findJobDetail(FindJobDetailReq findJobDetailReq) {
        return jobDetailService.findJobDetail(findJobDetailReq);
    }

    @ApiOperation("获取Job信息")
    @GetMapping("/job_detail/info/{jobGroup}/{jobName}" + JSON_SUFFIX)
    public JobDetailInfoRes getJobDetails(@PathVariable String jobGroup, @PathVariable String jobName) {
        return jobDetailService.getJobDetails(jobGroup, jobName);
    }

    @ApiOperation("保存Job")
    @PostMapping("/job_detail" + JSON_SUFFIX)
    public SaveJobDetailReq saveJobDetail(@Validated @RequestBody SaveJobDetailReq saveJobDetailReq) {
        jobDetailService.saveJobDetail(saveJobDetailReq);
        return saveJobDetailReq;
    }

    @ApiOperation("删除Job")
    @DeleteMapping("/job_detail" + JSON_SUFFIX)
    public JobDetail deleteJobDetail(@Validated JobDetailKeyReq jobDetailKeyReq) {
        return jobDetailService.deleteJobDetail(jobDetailKeyReq);
    }

    @ApiOperation("暂停Job")
    @PostMapping("/job_detail/pause" + JSON_SUFFIX)
    public JobDetail pauseJob(@Validated @RequestBody JobDetailKeyReq jobDetailKeyReq) {
        return jobDetailService.pauseJob(jobDetailKeyReq);
    }

    @ApiOperation("取消暂停Job")
    @PostMapping("/job_detail/resume" + JSON_SUFFIX)
    public JobDetail resumeJob(@Validated @RequestBody JobDetailKeyReq jobDetailKeyReq) {
        return jobDetailService.resumeJob(jobDetailKeyReq);
    }

    @ApiOperation("立即运行Job")
    @PostMapping("/job_detail/trigger" + JSON_SUFFIX)
    public JobDetail triggerJob(@Validated @RequestBody JobDetailKeyReq jobDetailKeyReq) {
        return jobDetailService.triggerJob(jobDetailKeyReq);
    }
}
