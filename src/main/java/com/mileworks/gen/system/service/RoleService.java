package com.mileworks.gen.system.service;

import com.mileworks.gen.common.domain.QueryRequest;
import com.mileworks.gen.common.service.IService;
import com.mileworks.gen.system.domain.Role;

import java.util.List;

public interface RoleService extends IService<Role> {

    List<Role> findRoles(Role role, QueryRequest request);

    List<Role> findUserRole(String userName);

    Role findByName(String roleName);

    void createRole(Role role);

    void deleteRoles(String[] roleIds) throws Exception;

    void updateRole(Role role) throws Exception;
}
