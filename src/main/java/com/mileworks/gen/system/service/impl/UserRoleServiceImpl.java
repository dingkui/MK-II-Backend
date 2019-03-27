package com.mileworks.gen.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mileworks.gen.system.dao.UserRoleMapper;
import com.mileworks.gen.system.domain.UserRole;
import com.mileworks.gen.system.service.UserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("userRoleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    @Transactional
    public void deleteUserRolesByRoleId(String[] roleIds) {
        List<String> list = Arrays.asList(roleIds);
        EntityWrapper<UserRole> roleMenuWrapper = new EntityWrapper<>();
        roleMenuWrapper.in("ROLE_ID", list);
        this.delete(roleMenuWrapper);
    }

    @Override
    @Transactional
    public void deleteUserRolesByUserId(String[] userIds) {
        List<String> list = Arrays.asList(userIds);
        EntityWrapper<UserRole> roleMenuWrapper = new EntityWrapper<>();
        roleMenuWrapper.in("USER_ID", list);
        this.delete(roleMenuWrapper);
    }

    @Override
    public List<String> findUserIdsByRoleId(String[] roleIds) {
        EntityWrapper<UserRole> userRoleWrapper = new EntityWrapper<>();
        userRoleWrapper.in("ROLE_ID", Arrays.asList(roleIds));
        List<UserRole> list = this.selectList(userRoleWrapper);

        return list.stream().map(userRole -> String.valueOf(userRole.getUserId())).collect(Collectors.toList());
    }

}
