package com.mileworks.gen.job.service;

import com.baomidou.mybatisplus.service.IService;
import com.mileworks.gen.job.domain.Job;

public interface JobService extends IService<Job> {

    Job findJob(Long jobId);

    void createJob(Job job);

    void updateJob(Job job);

    void deleteJobs(String[] jobIds);

    int updateBatch(String jobIds, String status);

    void run(String jobIds);

    void pause(String jobIds);

    void resume(String jobIds);

}
