package com.mileworks.gen.job.dao;


import com.mileworks.gen.common.config.MyMapper;
import com.mileworks.gen.job.domain.Job;

import java.util.List;

public interface JobMapper extends MyMapper<Job> {
	
	List<Job> queryList();
}