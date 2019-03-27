package com.mileworks.gen.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mileworks.gen.common.domain.MKConstant;
import com.mileworks.gen.common.domain.QueryRequest;
import com.mileworks.gen.common.service.CacheService;
import com.mileworks.gen.common.utils.MD5Util;
import com.mileworks.gen.system.dao.UserMapper;
import com.mileworks.gen.system.dao.UserRoleMapper;
import com.mileworks.gen.system.domain.User;
import com.mileworks.gen.system.domain.UserRole;
import com.mileworks.gen.system.manager.UserManager;
import com.mileworks.gen.system.service.UserConfigService;
import com.mileworks.gen.system.service.UserRoleService;
import com.mileworks.gen.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service("userService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserManager userManager;


    @Override
    public User findByName(String username) {
        User param = new User();
        param.setUsername(username);
        List<User> list = findUserDetail(param, new QueryRequest());
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public User findById(String userId) {
        EntityWrapper<User> userWrapper = new EntityWrapper<>();
        userWrapper.eq("user_id", Long.valueOf(userId));
        List<User> list = this.selectList(userWrapper);
        return list.isEmpty() ? null : list.get(0);
    }


    @Override
    public List<User> findUserDetail(User user, QueryRequest request) {
        try {
            if (request.getSortField() != null) {
                user.setSortField(request.getSortField());
                if (StringUtils.equals(MKConstant.ORDER_ASC, request.getSortOrder()))
                    user.setSortOrder("asc");
                else if (StringUtils.equals(MKConstant.ORDER_DESC, request.getSortOrder()))
                    user.setSortOrder("desc");
            }
            return this.baseMapper.findUserList(user);
        } catch (Exception e) {
            log.error("查询用户异常", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Page<User> findUserPage(Page<User> page, User user) {
        List<User> userList= this.baseMapper.findUserList(page, user);
        return page.setRecords(userList);
    }

    @Override
    @Transactional
    public void updateLoginTime(String username) throws Exception {
        User user = new User();
        user.setLastLoginTime(new Date());

        EntityWrapper<User> userWrapper = new EntityWrapper<>();
        userWrapper.eq("username", username);
        this.update(user, userWrapper);

        // 重新将用户信息加载到 redis中
        cacheService.saveUser(username);
    }

    @Override
    @Transactional
    public void createUser(User user) throws Exception {
        // 创建用户
        user.setCreateTime(new Date());
        user.setAvatar(User.DEFAULT_AVATAR);
        user.setPassword(MD5Util.encrypt(user.getUsername(), User.DEFAULT_PASSWORD));
        this.insert(user);

        // 保存用户角色
        String[] roles = user.getRoleId().split(",");
        setUserRoles(user, roles);

        // 创建用户默认的个性化配置
        userConfigService.initDefaultUserConfig(String.valueOf(user.getUserId()));

        // 将用户相关信息保存到 Redis中
        userManager.loadUserRedisCache(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) throws Exception {
        // 更新用户
        user.setPassword(null);
        user.setModifyTime(new Date());
        this.updateById(user);

        EntityWrapper<UserRole> userRoleWrapper = new EntityWrapper<>();
        userRoleWrapper.eq("user_id", user.getUserId());
        this.userRoleMapper.delete(userRoleWrapper);

        String[] roles = user.getRoleId().split(",");
        setUserRoles(user, roles);

        // 重新将用户信息，用户角色信息，用户权限信息 加载到 redis中
        cacheService.saveUser(user.getUsername());
        cacheService.saveRoles(user.getUsername());
        cacheService.savePermissions(user.getUsername());
    }

    @Override
    @Transactional
    public void deleteUsers(String[] userIds) throws Exception {
        // 先删除相应的缓存
        this.userManager.deleteUserRedisCache(userIds);

        List<String> list = Arrays.asList(userIds);
        this.deleteBatchIds(list);

        // 删除用户角色
        this.userRoleService.deleteUserRolesByUserId(userIds);
        // 删除用户个性化配置
        this.userConfigService.deleteByUserId(userIds);
    }

    @Override
    @Transactional
    public void updateProfile(User user) throws Exception {
        this.updateById(user);
        // 重新缓存用户信息
        cacheService.saveUser(user.getUsername());
    }

    @Override
    @Transactional
    public void updateAvatar(String username, String avatar) throws Exception {
        EntityWrapper<User> userRoleWrapper = new EntityWrapper<>();
        userRoleWrapper.eq("username", username);
        User user = new User();
        user.setAvatar(avatar);
        this.baseMapper.update(user, userRoleWrapper);
        // 重新缓存用户信息
        cacheService.saveUser(username);
    }

    @Override
    @Transactional
    public void updatePassword(String username, String password) throws Exception {
        EntityWrapper<User> userRoleWrapper = new EntityWrapper<>();
        userRoleWrapper.eq("username", username);
        User user = new User();
        user.setPassword(MD5Util.encrypt(username, password));
        this.baseMapper.update(user, userRoleWrapper);

        // 重新缓存用户信息
        cacheService.saveUser(username);
    }

    @Override
    @Transactional
    public void regist(String username, String password) throws Exception {
        User user = new User();
        user.setPassword(MD5Util.encrypt(username, password));
        user.setUsername(username);
        user.setCreateTime(new Date());
        user.setStatus(User.STATUS_VALID);
        user.setSsex(User.SEX_UNKNOW);
        user.setAvatar(User.DEFAULT_AVATAR);
        user.setDescription("注册用户");
        this.insert(user);

        UserRole ur = new UserRole();
        ur.setUserId(user.getUserId());
        ur.setRoleId(2L); // 注册用户角色 ID
        this.userRoleMapper.insert(ur);

        // 创建用户默认的个性化配置
        userConfigService.initDefaultUserConfig(String.valueOf(user.getUserId()));
        // 将用户相关信息保存到 Redis中
        userManager.loadUserRedisCache(user);

    }

    @Override
    @Transactional
    public void resetPassword(String[] usernames) throws Exception {
        for (String username : usernames) {
            EntityWrapper<User> userRoleWrapper = new EntityWrapper<>();
            userRoleWrapper.eq("username", username);
            User user = new User();
            user.setPassword(MD5Util.encrypt(username, User.DEFAULT_PASSWORD));
            this.baseMapper.update(user, userRoleWrapper);

            // 重新将用户信息加载到 redis中
            cacheService.saveUser(username);
        }

    }

    private void setUserRoles(User user, String[] roles) {
        Arrays.stream(roles).forEach(roleId -> {
            UserRole ur = new UserRole();
            ur.setUserId(user.getUserId());
            ur.setRoleId(Long.valueOf(roleId));
            this.userRoleMapper.insert(ur);
        });
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.encrypt("mk", User.DEFAULT_PASSWORD));
        System.out.println(MD5Util.encrypt("admin", User.DEFAULT_PASSWORD));
        System.out.println(MD5Util.encrypt("jack", User.DEFAULT_PASSWORD));
    }
}
