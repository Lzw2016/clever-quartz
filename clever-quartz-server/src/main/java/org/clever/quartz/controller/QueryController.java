package org.clever.quartz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.server.controller.BaseController;
import org.clever.quartz.dto.request.FindJobDetailReq;
import org.clever.quartz.dto.response.JobAndTriggerRes;
import org.clever.quartz.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-29 21:20 <br/>
 */
@Api(description = "查询接口")
@RequestMapping(value = "/api/query")
@RestController
public class QueryController extends BaseController {

    @Autowired
    private QueryService queryService;

    @ApiOperation("查询任务及其触发器")
    @GetMapping("/job_and_trigger" + JSON_SUFFIX)
    public IPage<JobAndTriggerRes> queryJobAndTrigger(FindJobDetailReq req) {
        return queryService.queryJobDetailsAndTriggers(req);
    }
}
