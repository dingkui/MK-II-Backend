package com.mileworks.gen.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mileworks.gen.common.domain.QueryRequest;
import com.mileworks.gen.common.domain.Tree;
import com.mileworks.gen.common.utils.TreeUtil;
import com.mileworks.gen.system.dao.DeptMapper;
import com.mileworks.gen.system.domain.Dept;
import com.mileworks.gen.system.service.DeptService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("deptService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

	@Override
	public Map<String, Object> findDepts(QueryRequest request, Dept dept) {
		Map<String, Object> result = new HashMap<>();
		try {
			List<Dept> depts = findDepts(dept, request);
			List<Tree<Dept>> trees = new ArrayList<>();
			buildTrees(trees, depts);
			Tree<Dept> deptTree = TreeUtil.build(trees);

			result.put("rows", deptTree);
			result.put("total", depts.size());
		} catch (Exception e) {
			log.error("获取部门列表失败", e);
			result.put("rows", null);
			result.put("total", 0);
		}
		return result;
	}

	@Override
	public List<Dept> findDepts(Dept dept, QueryRequest request) {
		EntityWrapper<Dept> deptWrapper = new EntityWrapper<>();
		deptWrapper.orderBy(request.getSortField());
		if (StringUtils.isNotBlank(dept.getDeptName()))
			deptWrapper.eq("dept_name", dept.getDeptName());
		if (StringUtils.isNotBlank(dept.getCreateTimeFrom()) && StringUtils.isNotBlank(dept.getCreateTimeTo())) {
			deptWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') >= {0}", dept.getCreateTimeFrom());
			deptWrapper.and("date_format(CREATE_TIME,'%Y-%m-%d') <= {0}", dept.getCreateTimeTo());
		}
		return this.selectList(deptWrapper);
	}

	@Override
	@Transactional
	public void createDept(Dept dept) {
		Long parentId = dept.getParentId();
		if (parentId == null)
			dept.setParentId(0L);
		dept.setCreateTime(new Date());
		this.insert(dept);
	}

	@Override
	@Transactional
	public void updateDept(Dept dept) {
		dept.setModifyTime(new Date());
		this.updateById(dept);
	}

	@Override
	@Transactional
	public void deleteDepts(String[] deptIds) {
		Arrays.stream(deptIds).forEach(deptId -> this.baseMapper.deleteDepts(deptId));
	}

	private void buildTrees(List<Tree<Dept>> trees, List<Dept> depts) {
		depts.forEach(dept -> {
			Tree<Dept> tree = new Tree<>();
			tree.setId(dept.getDeptId().toString());
			tree.setKey(tree.getId());
			tree.setParentId(dept.getParentId().toString());
			tree.setText(dept.getDeptName());
			tree.setCreateTime(dept.getCreateTime());
			tree.setModifyTime(dept.getModifyTime());
			tree.setOrder(dept.getOrderNum());
			tree.setTitle(tree.getText());
			tree.setValue(tree.getId());
			trees.add(tree);
		});
	}
}
