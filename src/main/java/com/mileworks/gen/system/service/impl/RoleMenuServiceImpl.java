package com.mileworks.gen.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mileworks.gen.system.dao.RoleMenuMapper;
import com.mileworks.gen.system.domain.RoleMenu;
import com.mileworks.gen.system.service.RoleMenuServie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service("roleMenuService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuServie {

    @Override
    @Transactional
    public void deleteRoleMenusByRoleId(String[] roleIds) {
        List<String> list = Arrays.asList(roleIds);
        EntityWrapper<RoleMenu> roleMenuWrapper = new EntityWrapper<>();
        roleMenuWrapper.in("roleId", list);
        this.delete(roleMenuWrapper);
    }

    @Override
    @Transactional
    public void deleteRoleMenusByMenuId(String[] menuIds) {
        List<String> list = Arrays.asList(menuIds);
        EntityWrapper<RoleMenu> roleMenuWrapper = new EntityWrapper<>();
        roleMenuWrapper.in("menuId", list);
        this.delete(roleMenuWrapper);
    }

    @Override
    public List<RoleMenu> getRoleMenusByRoleId(String roleId) {
        EntityWrapper<RoleMenu> roleMenuWrapper = new EntityWrapper<>();
        roleMenuWrapper.eq("role_id", roleId);
        return this.selectList(roleMenuWrapper);
    }

}
