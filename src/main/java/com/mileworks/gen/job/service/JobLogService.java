package com.mileworks.gen.job.service;

import com.mileworks.gen.common.domain.QueryRequest;
import com.mileworks.gen.common.service.IService;
import com.mileworks.gen.job.domain.JobLog;

import java.util.List;

public interface JobLogService extends IService<JobLog> {

    List<JobLog> findJobLogs(QueryRequest request, JobLog jobLog);

    void saveJobLog(JobLog log);

    void deleteJobLogs(String[] jobLogIds);
}
