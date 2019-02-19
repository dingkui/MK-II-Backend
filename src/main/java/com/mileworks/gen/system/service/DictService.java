package com.mileworks.gen.system.service;

import com.mileworks.gen.common.domain.QueryRequest;
import com.mileworks.gen.common.service.IService;
import com.mileworks.gen.system.domain.Dict;

import java.util.List;

public interface DictService extends IService<Dict> {

    List<Dict> findDicts(QueryRequest request, Dict dict);

    void createDict(Dict dict);

    void updateDict(Dict dicdt);

    void deleteDicts(String[] dictIds);

}
