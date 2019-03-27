package com.mileworks.gen.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mileworks.gen.common.domain.QueryRequest;
import com.mileworks.gen.system.dao.DictMapper;
import com.mileworks.gen.system.domain.Dict;
import com.mileworks.gen.system.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service("dictService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Override
    public List<Dict> findDicts(QueryRequest request, Dict dict) {
        try {
            EntityWrapper<Dict> dictWrapper = new EntityWrapper<>();
            dictWrapper.orderBy(request.getSortField());

            if (StringUtils.isNotBlank(dict.getKeyy())) {
                dictWrapper.eq("keyy", Long.valueOf(dict.getKeyy()));
            }
            if (StringUtils.isNotBlank(dict.getValuee())) {
                dictWrapper.eq("valuee", dict.getValuee());
            }
            if (StringUtils.isNotBlank(dict.getTableName())) {
                dictWrapper.eq("table_name", dict.getTableName());
            }
            if (StringUtils.isNotBlank(dict.getFieldName())) {
                dictWrapper.eq("field_name", dict.getFieldName());
            }
            Page<Dict> jobLogPage = new Page<>(request.getPageNum(), request.getPageSize());
            return this.selectPage(jobLogPage, dictWrapper).getRecords();
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public void createDict(Dict dict) {
        this.insert(dict);
    }

    @Override
    @Transactional
    public void updateDict(Dict dict) {
        this.updateById(dict);
    }

    @Override
    @Transactional
    public void deleteDicts(String[] dictIds) {
        List<String> list = Arrays.asList(dictIds);
        this.deleteBatchIds(list);
    }
}
