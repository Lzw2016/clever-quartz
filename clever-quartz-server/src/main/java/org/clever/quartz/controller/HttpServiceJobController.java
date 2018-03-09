package org.clever.quartz.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.model.response.AjaxMessage;
import org.clever.common.server.controller.BaseController;
import org.clever.quartz.dto.request.AddHttpServiceJobReq;
import org.clever.quartz.service.HttpServiceJobService;
import org.clever.quartz.service.QuartzTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Http任务 Controller
 * Created by lzw on 2017/6/4.
 */
@Api(description = "HTTP Job管理")
@RequestMapping(value = "/api/quartz")
@RestController
public class HttpServiceJobController extends BaseController {

    @Autowired
    private HttpServiceJobService httpServiceJobService;
    @Autowired
    private QuartzTriggerService triggerService;
//    @Autowired
//    private QuartzJobDetailService jobDetailService;
//    @Autowired
//    private JobLogService qrtzJobLogService;

    @ApiOperation("增加HTTP任务")
    @PostMapping("/http_job" + JSON_SUFFIX)
    public AjaxMessage<String> addHttpServiceJob(@Validated @RequestBody AddHttpServiceJobReq addHttpServiceJobReq) {
        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "新增任务成功", null);
        if (triggerService.validatorCron(addHttpServiceJobReq.getCron(), 1, ajaxMessage) == null) {
            return ajaxMessage;
        }
        httpServiceJobService.addHttpServiceJob(addHttpServiceJobReq, ajaxMessage);
        return ajaxMessage;
    }

//    @ApiOperation("查询HTTP任务")
//    @GetMapping("/http_job" + JSON_SUFFIX)
//    public AjaxMessage<List<QuartzJobDetails>> findJobDetail(FindJobDetailReq findJobDetailVo) {
//        AjaxMessage<List<QuartzJobDetails>> ajaxMessage = new AjaxMessage<>(true, "查询任务成功", null);
//        List<QuartzJobDetails> jobDetailList = jobDetailService.findJobDetail(findJobDetailVo);
//        ajaxMessage.setResult(jobDetailList);
//        return ajaxMessage;
//    }

//    @ApiOperation("暂停、继续运行HTTP任务")
//    @PutMapping("/http_job" + JSON_SUFFIX)
//    public AjaxMessage<String> pauseOrResumeJob(@Validated @RequestBody PauseOrResumeJobVo pauseOrResumeJobVo) {
//        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "更新任务状态成功", null);
//        boolean flag;
//        JobDetailKeyReq jobDetailKeyVo = new JobDetailKeyReq(pauseOrResumeJobVo.getJobName(), pauseOrResumeJobVo.getJobGroup());
//        if ("pause".equalsIgnoreCase(pauseOrResumeJobVo.getStatus())) {
//            flag = jobDetailService.pauseJob(jobDetailKeyVo, ajaxMessage);
//            if (flag) {
//                ajaxMessage.setSuccessMessage("暂停定时任务成功");
//            }
//        } else if ("resume".equalsIgnoreCase(pauseOrResumeJobVo.getStatus())) {
//            flag = jobDetailService.resumeJob(jobDetailKeyVo, ajaxMessage);
//            if (flag) {
//                ajaxMessage.setSuccessMessage("继续运行定时任务成功");
//            }
//        } else if ("trigger".equalsIgnoreCase(pauseOrResumeJobVo.getStatus())) {
//            flag = jobDetailService.triggerJob(jobDetailKeyVo, ajaxMessage);
//            if (flag) {
//                ajaxMessage.setSuccessMessage("立即执行定时任务成功");
//            }
//        } else {
//            ajaxMessage.setSuccess(false);
//            ajaxMessage.setFailMessage("不能识别的操作:[" + pauseOrResumeJobVo.getStatus() + "]");
//        }
//        return ajaxMessage;
//    }

//    @ApiOperation("停止并且删除HTTP任务")
//    @DeleteMapping("/http_job/{jobGroup}/{jobName}" + JSON_SUFFIX)
//    public AjaxMessage<String> deleteJobDetail(@PathVariable() String jobGroup, @PathVariable String jobName) {
//        AjaxMessage<String> ajaxMessage = new AjaxMessage<>(true, "删除JobDetail成功", null);
//        jobDetailService.deleteJobDetail(new JobDetailKeyReq(jobName, jobGroup), ajaxMessage);
//        return ajaxMessage;
//    }
}
