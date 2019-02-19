package com.mileworks.gen.job.controller;

import com.mileworks.gen.common.controller.BaseController;
import com.mileworks.gen.common.domain.QueryRequest;
import com.mileworks.gen.common.exception.MKException;
import com.mileworks.gen.job.domain.JobLog;
import com.mileworks.gen.job.service.JobLogService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("job/log")
public class JobLogController extends BaseController {

    private String message;

    @Autowired
    private JobLogService jobLogService;

    @GetMapping
    @RequiresPermissions("jobLog:view")
    public Map<String, Object> jobLogList(QueryRequest request, JobLog log) {
        return super.selectByPageNumSize(request, () -> this.jobLogService.findJobLogs(request, log));
    }

    @DeleteMapping("/{jobIds}")
    @RequiresPermissions("jobLog:delete")
    public void deleteJobLog(@NotBlank(message = "{required}") @PathVariable String jobIds) throws MKException {
        try {
            String[] ids = jobIds.split(",");
            this.jobLogService.deleteJobLogs(ids);
        } catch (Exception e) {
            message = "删除调度日志失败";
            log.error(message, e);
            throw new MKException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("jobLog:export")
    public void export(JobLog jobLog, QueryRequest request, HttpServletResponse response) throws MKException {
        try {
            List<JobLog> jobLogs = this.jobLogService.findJobLogs(request, jobLog);
            ExcelKit.$Export(JobLog.class, response).downXlsx(jobLogs, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new MKException(message);
        }
    }
}
