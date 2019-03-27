package com.mileworks.gen.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mileworks.gen.system.domain.Dept;

public interface DeptMapper extends BaseMapper<Dept> {

	/**
	 * 递归删除部门
	 *
	 * @param deptId deptId
	 */
	void deleteDepts(String deptId);
}