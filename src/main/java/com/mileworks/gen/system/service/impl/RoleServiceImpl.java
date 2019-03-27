package com.mileworks.gen.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mileworks.gen.system.dao.RoleMapper;
import com.mileworks.gen.system.dao.RoleMenuMapper;
import com.mileworks.gen.system.domain.Role;
import com.mileworks.gen.system.domain.RoleMenu;
import com.mileworks.gen.system.manager.UserManager;
import com.mileworks.gen.system.service.RoleMenuServie;
import com.mileworks.gen.system.service.RoleService;
import com.mileworks.gen.system.service.UserRoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("roleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleMenuServie roleMenuService;
    @Autowired
    private UserManager userManager;

    @Override
    public List<Role> findUserRole(String userName) {
//        return this.roleMenuMapper.findUserRole(userName);
        return new ArrayList<>();
    }

    @Override
    public Role findByName(String roleName) {
        EntityWrapper<Role> roleWrapper = new EntityWrapper<>();
        roleWrapper.and("lower(role_name)={0}", roleName.toLowerCase());
        List<Role> list = this.selectList(roleWrapper);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void createRole(Role role) {
        role.setCreateTime(new Date());
        this.insert(role);

        String[] menuIds = role.getMenuId().split(",");
        setRoleMenus(role, menuIds);
    }

    @Override
    public void deleteRoles(String[] roleIds) throws Exception {
        // 查找这些角色关联了那些用户
        List<String> userIds = this.userRoleService.findUserIdsByRoleId(roleIds);

        List<String> list = Arrays.asList(roleIds);
        this.deleteBatchIds(list);

        this.roleMenuService.deleteRoleMenusByRoleId(roleIds);
        this.userRoleService.deleteUserRolesByRoleId(roleIds);

        // 重新将这些用户的角色和权限缓存到 Redis中
        this.userManager.loadUserPermissionRoleRedisCache(userIds);
    }

    @Override
    public void updateRole(Role role) throws Exception {
        // 查找这些角色关联了那些用户
        String[] roleId = {String.valueOf(role.getRoleId())};
        List<String> userIds = this.userRoleService.findUserIdsByRoleId(roleId);

        role.setModifyTime(new Date());
        this.updateById(role);

        EntityWrapper<RoleMenu> roleMenuWrapper = new EntityWrapper<>();
        roleMenuWrapper.eq("role_id", role.getRoleId());
        this.roleMenuMapper.delete(roleMenuWrapper);

        String[] menuIds = role.getMenuId().split(",");
        setRoleMenus(role, menuIds);

        // 重新将这些用户的角色和权限缓存到 Redis中
        this.userManager.loadUserPermissionRoleRedisCache(userIds);
    }

    private void setRoleMenus(Role role, String[] menuIds) {
        Arrays.stream(menuIds).forEach(menuId -> {
            RoleMenu rm = new RoleMenu();
            rm.setMenuId(Long.valueOf(menuId));
            rm.setRoleId(role.getRoleId());
            this.roleMenuMapper.insert(rm);
        });
    }
}
