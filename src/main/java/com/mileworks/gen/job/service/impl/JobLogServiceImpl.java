package com.mileworks.gen.job.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mileworks.gen.job.dao.JobLogMapper;
import com.mileworks.gen.job.domain.JobLog;
import com.mileworks.gen.job.service.JobLogService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("JobLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper, JobLog> implements JobLogService {

    @Override
    @Transactional
    public void saveJobLog(JobLog log) {
        this.insert(log);
    }

    @Override
    @Transactional
    public void deleteJobLogs(String[] jobLogIds) {
        List<String> list = Arrays.asList(jobLogIds);
        this.deleteBatchIds(list);
    }

}
