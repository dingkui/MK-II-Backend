package com.mileworks.gen.system.dao;

import com.mileworks.gen.common.config.MyMapper;
import com.mileworks.gen.system.domain.Role;

import java.util.List;

public interface RoleMapper extends MyMapper<Role> {
	
	List<Role> findUserRole(String userName);
	
}