package com.mileworks.gen.system.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.mileworks.gen.system.domain.Role;

public interface RoleService extends IService<Role> {

	List<Role> findUserRole(String userName);

	Role findByName(String roleName);

	void createRole(Role role);

	void deleteRoles(String[] roleIds) throws Exception;

	void updateRole(Role role) throws Exception;
}
