package com.mileworks.gen.system.dao;

import com.mileworks.gen.common.config.MyMapper;
import com.mileworks.gen.system.domain.Dept;

public interface DeptMapper extends MyMapper<Dept> {

	/**
	 * 递归删除部门
	 *
	 * @param deptId deptId
	 */
	void deleteDepts(String deptId);
}