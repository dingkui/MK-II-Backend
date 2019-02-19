package com.mileworks.gen.system.service;


import com.mileworks.gen.common.domain.QueryRequest;
import com.mileworks.gen.common.service.IService;
import com.mileworks.gen.system.domain.Dept;

import java.util.List;
import java.util.Map;

public interface DeptService extends IService<Dept> {

    Map<String, Object> findDepts(QueryRequest request, Dept dept);

    List<Dept> findDepts(Dept dept, QueryRequest request);

    void createDept(Dept dept);

    void updateDept(Dept dept);

    void deleteDepts(String[] deptIds);
}
