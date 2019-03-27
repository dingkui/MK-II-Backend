package com.mileworks.gen.job.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.mileworks.gen.common.domain.QueryRequest;
import com.mileworks.gen.common.exception.MKException;
import com.mileworks.gen.job.domain.JobLog;
import com.mileworks.gen.job.service.JobLogService;
import com.wuwenze.poi.ExcelKit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("job/log")
public class JobLogController {

	private String message;

	@Autowired
	private JobLogService jobLogService;

	@GetMapping
	@RequiresPermissions("jobLog:view")
	public Page<JobLog> jobLogList(QueryRequest request, JobLog jobLog) {
		EntityWrapper<JobLog> jobLogWrapper = new EntityWrapper<>();
		jobLogWrapper.orderBy(request.getSortField());

		if (StringUtils.isNotBlank(jobLog.getBeanName())) {
			jobLogWrapper.eq("bean_name", jobLog.getBeanName());
		}
		if (StringUtils.isNotBlank(jobLog.getMethodName())) {
			jobLogWrapper.eq("method_name", jobLog.getMethodName());
		}
		if (StringUtils.isNotBlank(jobLog.getParams())) {
			jobLogWrapper.like("params", jobLog.getParams());
		}
		if (StringUtils.isNotBlank(jobLog.getStatus())) {
			jobLogWrapper.eq("status", Long.valueOf(jobLog.getStatus()));
		}
		if (StringUtils.isNotBlank(jobLog.getCreateTimeFrom()) && StringUtils.isNotBlank(jobLog.getCreateTimeTo())) {
			jobLogWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') >= {0}", jobLog.getCreateTimeFrom());
			jobLogWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') <= {0}", jobLog.getCreateTimeTo());
		}
		Page<JobLog> jobLogPage = new Page<>(request.getPageNum(), request.getPageSize());
		return this.jobLogService.selectPage(jobLogPage, jobLogWrapper);
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
			EntityWrapper<JobLog> jobLogWrapper = new EntityWrapper<>();
			jobLogWrapper.orderBy(request.getSortField());

			if (StringUtils.isNotBlank(jobLog.getBeanName())) {
				jobLogWrapper.eq("bean_name", jobLog.getBeanName());
			}
			if (StringUtils.isNotBlank(jobLog.getMethodName())) {
				jobLogWrapper.eq("method_name", jobLog.getMethodName());
			}
			if (StringUtils.isNotBlank(jobLog.getParams())) {
				jobLogWrapper.like("params", jobLog.getParams());
			}
			if (StringUtils.isNotBlank(jobLog.getStatus())) {
				jobLogWrapper.eq("status", Long.valueOf(jobLog.getStatus()));
			}
			if (StringUtils.isNotBlank(jobLog.getCreateTimeFrom()) && StringUtils.isNotBlank(jobLog.getCreateTimeTo())) {
				jobLogWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') >= {0}", jobLog.getCreateTimeFrom());
				jobLogWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') <= {0}", jobLog.getCreateTimeTo());
			}
			List<JobLog> jobLogs = this.jobLogService.selectList(jobLogWrapper);
			ExcelKit.$Export(JobLog.class, response).downXlsx(jobLogs, false);
		} catch (Exception e) {
			message = "导出Excel失败";
			log.error(message, e);
			throw new MKException(message);
		}
	}
}
