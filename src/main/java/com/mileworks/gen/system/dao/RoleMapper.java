package com.mileworks.gen.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mileworks.gen.system.domain.Role;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
	
	List<Role> findUserRole(String userName);
	
}