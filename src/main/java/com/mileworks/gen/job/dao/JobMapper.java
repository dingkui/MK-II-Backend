package com.mileworks.gen.job.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mileworks.gen.job.domain.Job;

import java.util.List;

public interface JobMapper extends BaseMapper<Job> {
	
	List<Job> queryList();
}