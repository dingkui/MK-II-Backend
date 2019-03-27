package com.mileworks.gen.job.service;

import com.baomidou.mybatisplus.service.IService;
import com.mileworks.gen.job.domain.JobLog;

public interface JobLogService extends IService<JobLog> {

    void saveJobLog(JobLog log);

    void deleteJobLogs(String[] jobLogIds);
}
