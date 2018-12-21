package org.clever.quartz.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("增加HTTP任务")
    @PostMapping("/http_job" + JSON_SUFFIX)
    public AddHttpServiceJobReq addHttpServiceJob(@Validated @RequestBody AddHttpServiceJobReq addHttpServiceJobReq) {
        addHttpServiceJobReq.getHttpJobConfig().valid();
        addHttpServiceJobReq.getNotice().valid();
        triggerService.validatorCron(addHttpServiceJobReq.getCron(), 1);
        httpServiceJobService.addHttpServiceJob(addHttpServiceJobReq);
        return addHttpServiceJobReq;
    }
}
