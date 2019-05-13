package com.mileworks.gen.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.mileworks.gen.common.annotation.Log;
import com.mileworks.gen.common.domain.QueryRequest;
import com.mileworks.gen.common.exception.MKException;
import com.mileworks.gen.system.domain.SysLog;
import com.mileworks.gen.system.service.LogService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("log")
public class LogController {

    private String message;

    @Autowired
    private LogService logService;

    @GetMapping
    @RequiresPermissions("log:view")
    public Page<SysLog> logList(QueryRequest request, SysLog log) {

        EntityWrapper<SysLog> dictWrapper = new EntityWrapper<>();
        // 排序
        if (StringUtils.isBlank(request.getSortField())) {
            dictWrapper.orderBy("create_time", false);
        } else {
            dictWrapper.orderBy(request.getSortField());
        }

        // 查询条件
        if (StringUtils.isNotBlank(log.getUsername())) {
            dictWrapper.eq("username=", log.getUsername().toLowerCase());
        }
        if (StringUtils.isNotBlank(log.getOperation())) {
            dictWrapper.like("operation", log.getOperation());
        }
        if (StringUtils.isNotBlank(log.getLocation())) {
            dictWrapper.like("location", log.getLocation());
        }
        if (StringUtils.isNotBlank(log.getCreateTimeFrom()) && StringUtils.isNotBlank(log.getCreateTimeTo())) {
            dictWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') >= {0}", log.getCreateTimeFrom());
            dictWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') <= {0}", log.getCreateTimeTo());
        }

        Page<SysLog> sysLogPage = new Page<>(request.getPageNum(), request.getPageSize());
        return this.logService.selectPage(sysLogPage, dictWrapper);
    }

    @Log("删除系统日志")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("log:delete")
    public void deleteLogss(@NotBlank(message = "{required}") @PathVariable String ids) throws MKException {
        try {
            String[] logIds = ids.split(",");
            this.logService.deleteLogs(logIds);
        } catch (Exception e) {
            message = "删除日志失败";
            log.error(message, e);
            throw new MKException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("log:export")
    public void export(SysLog sysLog, QueryRequest request, HttpServletResponse response) throws MKException {
        try {

            EntityWrapper<SysLog> sysLogWrapper = new EntityWrapper<>();
            // 排序
            if (StringUtils.isBlank(request.getSortField())) {
                sysLogWrapper.orderBy("create_time", false);
            } else {
                sysLogWrapper.orderBy(request.getSortField());
            }
            // 查询条件
            if (StringUtils.isNotBlank(sysLog.getUsername())) {
                sysLogWrapper.eq("username=", sysLog.getUsername().toLowerCase());
            }
            if (StringUtils.isNotBlank(sysLog.getOperation())) {
                sysLogWrapper.like("operation", sysLog.getOperation());
            }
            if (StringUtils.isNotBlank(sysLog.getLocation())) {
                sysLogWrapper.like("location", sysLog.getLocation());
            }
            if (StringUtils.isNotBlank(sysLog.getCreateTimeFrom()) && StringUtils.isNotBlank(sysLog.getCreateTimeTo())) {
                sysLogWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') >= {0}", sysLog.getCreateTimeFrom());
                sysLogWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') <= {0}", sysLog.getCreateTimeTo());
            }

            List<SysLog> sysLogs = this.logService.selectList(sysLogWrapper);
            ExcelKit.$Export(SysLog.class, response).downXlsx(sysLogs, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new MKException(message);
        }
    }
}
