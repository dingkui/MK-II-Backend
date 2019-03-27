package com.mileworks.gen.system.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

import com.baomidou.mybatisplus.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mileworks.gen.system.domain.SysLog;

public interface LogService extends IService<SysLog> {

    void deleteLogs(String[] logIds);

    @Async
    void saveLog(ProceedingJoinPoint point, SysLog log) throws JsonProcessingException;
}
