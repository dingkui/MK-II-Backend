package com.mileworks.gen.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.mileworks.gen.system.domain.Test;

import java.util.List;

public interface TestService extends IService<Test> {

    List<Test> findTests();

    /**
     * 批量插入
     * @param list List<Test>
     */
    void batchInsert(List<Test> list);
}
