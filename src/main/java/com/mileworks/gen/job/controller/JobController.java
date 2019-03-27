package com.mileworks.gen.job.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.mileworks.gen.common.annotation.Log;
import com.mileworks.gen.common.domain.QueryRequest;
import com.mileworks.gen.common.exception.MKException;
import com.mileworks.gen.job.domain.Job;
import com.mileworks.gen.job.service.JobService;
import com.wuwenze.poi.ExcelKit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("job")
public class JobController {

	private String message;

	@Autowired
	private JobService jobService;

	@GetMapping
	@RequiresPermissions("job:view")
	public Page<Job> jobList(QueryRequest request, Job job) {
		EntityWrapper<Job> jobWrapper = new EntityWrapper<>();
		jobWrapper.orderBy(request.getSortField());

		if (StringUtils.isNotBlank(job.getBeanName())) {
			jobWrapper.eq("bean_name", job.getBeanName());
		}
		if (StringUtils.isNotBlank(job.getMethodName())) {
			jobWrapper.eq("method_name", job.getMethodName());
		}
		if (StringUtils.isNotBlank(job.getParams())) {
			jobWrapper.like("params", job.getParams());
		}
		if (StringUtils.isNotBlank(job.getRemark())) {
			jobWrapper.like("remark", job.getRemark());
		}
		if (StringUtils.isNotBlank(job.getStatus())) {
			jobWrapper.eq("status", Long.valueOf(job.getStatus()));
		}
		if (StringUtils.isNotBlank(job.getCreateTimeFrom()) && StringUtils.isNotBlank(job.getCreateTimeTo())) {
			jobWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') >= {0}", job.getCreateTimeFrom());
			jobWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') <= {0}", job.getCreateTimeTo());
		}
		Page<Job> jobPage = new Page<>(request.getPageNum(), request.getPageSize());
		return this.jobService.selectPage(jobPage, jobWrapper);
	}

	@GetMapping("cron/check")
	public boolean checkCron(String cron) {
		try {
			return CronExpression.isValidExpression(cron);
		} catch (Exception e) {
			return false;
		}
	}

	@Log("新增定时任务")
	@PostMapping
	@RequiresPermissions("job:add")
	public void addJob(@Valid Job job) throws MKException {
		try {
			this.jobService.createJob(job);
		} catch (Exception e) {
			message = "新增定时任务失败";
			log.error(message, e);
			throw new MKException(message);
		}
	}

	@Log("删除定时任务")
	@DeleteMapping("/{jobIds}")
	@RequiresPermissions("job:delete")
	public void deleteJob(@NotBlank(message = "{required}") @PathVariable String jobIds) throws MKException {
		try {
			String[] ids = jobIds.split(",");
			this.jobService.deleteJobs(ids);
		} catch (Exception e) {
			message = "删除定时任务失败";
			log.error(message, e);
			throw new MKException(message);
		}
	}

	@Log("修改定时任务")
	@PutMapping
	@RequiresPermissions("job:update")
	public void updateJob(@Valid Job job) throws MKException {
		try {
			this.jobService.updateJob(job);
		} catch (Exception e) {
			message = "修改定时任务失败";
			log.error(message, e);
			throw new MKException(message);
		}
	}

	@Log("执行定时任务")
	@GetMapping("run/{jobId}")
	@RequiresPermissions("job:run")
	public void runJob(@NotBlank(message = "{required}") @PathVariable String jobId) throws MKException {
		try {
			this.jobService.run(jobId);
		} catch (Exception e) {
			message = "执行定时任务失败";
			log.error(message, e);
			throw new MKException(message);
		}
	}

	@Log("暂停定时任务")
	@GetMapping("pause/{jobId}")
	@RequiresPermissions("job:pause")
	public void pauseJob(@NotBlank(message = "{required}") @PathVariable String jobId) throws MKException {
		try {
			this.jobService.pause(jobId);
		} catch (Exception e) {
			message = "暂停定时任务失败";
			log.error(message, e);
			throw new MKException(message);
		}
	}

	@Log("恢复定时任务")
	@GetMapping("resume/{jobId}")
	@RequiresPermissions("job:resume")
	public void resumeJob(@NotBlank(message = "{required}") @PathVariable String jobId) throws MKException {
		try {
			this.jobService.resume(jobId);
		} catch (Exception e) {
			message = "恢复定时任务失败";
			log.error(message, e);
			throw new MKException(message);
		}
	}

	@PostMapping("excel")
	@RequiresPermissions("job:export")
	public void export(Job job, QueryRequest request, HttpServletResponse response) throws MKException {
		try {
			EntityWrapper<Job> jobWrapper = new EntityWrapper<>();
			jobWrapper.orderBy(request.getSortField());

			if (StringUtils.isNotBlank(job.getBeanName())) {
				jobWrapper.eq("bean_name", job.getBeanName());
			}
			if (StringUtils.isNotBlank(job.getMethodName())) {
				jobWrapper.eq("method_name", job.getMethodName());
			}
			if (StringUtils.isNotBlank(job.getParams())) {
				jobWrapper.like("params", job.getParams());
			}
			if (StringUtils.isNotBlank(job.getRemark())) {
				jobWrapper.like("remark", job.getRemark());
			}
			if (StringUtils.isNotBlank(job.getStatus())) {
				jobWrapper.eq("status", Long.valueOf(job.getStatus()));
			}
			if (StringUtils.isNotBlank(job.getCreateTimeFrom()) && StringUtils.isNotBlank(job.getCreateTimeTo())) {
				jobWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') >= {0}", job.getCreateTimeFrom());
				jobWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') <= {0}", job.getCreateTimeTo());
			}
			List<Job> jobs = this.jobService.selectList(jobWrapper);
			ExcelKit.$Export(Job.class, response).downXlsx(jobs, false);
		} catch (Exception e) {
			message = "导出Excel失败";
			log.error(message, e);
			throw new MKException(message);
		}
	}
}
