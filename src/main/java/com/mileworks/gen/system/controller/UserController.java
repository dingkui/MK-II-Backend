package com.mileworks.gen.system.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.mileworks.gen.common.annotation.Log;
import com.mileworks.gen.common.domain.QueryRequest;
import com.mileworks.gen.common.exception.MKException;
import com.mileworks.gen.common.utils.MD5Util;
import com.mileworks.gen.system.domain.User;
import com.mileworks.gen.system.domain.UserConfig;
import com.mileworks.gen.system.service.UserConfigService;
import com.mileworks.gen.system.service.UserService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("user")
public class UserController {

    private String message;

    @Autowired
    private UserService userService;
    @Autowired
    private UserConfigService userConfigService;

    @GetMapping("check/{username}")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userService.findByName(username) == null;
    }

    @GetMapping("/{username}")
    public User detail(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userService.findByName(username);
    }

    @GetMapping
    @RequiresPermissions("user:view")
    public Page<User> userList(QueryRequest request, User user) {
        Page<User> userPage = new Page<>(request.getPageNum(),request.getPageSize());
        return this.userService.findUserPage(userPage,user);
    }

    @Log("新增用户")
    @PostMapping
    @RequiresPermissions("user:add")
    public void addUser(@Valid User user) throws MKException {
        try {
            this.userService.createUser(user);
        } catch (Exception e) {
            message = "新增用户失败";
            log.error(message, e);
            throw new MKException(message);
        }
    }

    @Log("修改用户")
    @PutMapping
    @RequiresPermissions("user:update")
    public void updateUser(@Valid User user) throws MKException {
        try {
            this.userService.updateUser(user);
        } catch (Exception e) {
            message = "修改用户失败";
            log.error(message, e);
            throw new MKException(message);
        }
    }

    @Log("删除用户")
    @DeleteMapping("/{userIds}")
    @RequiresPermissions("user:delete")
    public void deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) throws MKException {
        try {
            String[] ids = userIds.split(",");
            this.userService.deleteUsers(ids);
        } catch (Exception e) {
            message = "删除用户失败";
            log.error(message, e);
            throw new MKException(message);
        }
    }

    @PutMapping("profile")
    public void updateProfile(@Valid User user) throws MKException {
        try {
            this.userService.updateProfile(user);
        } catch (Exception e) {
            message = "修改个人信息失败";
            log.error(message, e);
            throw new MKException(message);
        }
    }

    @PutMapping("avatar")
    public void updateAvatar(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String avatar) throws MKException {
        try {
            this.userService.updateAvatar(username, avatar);
        } catch (Exception e) {
            message = "修改头像失败";
            log.error(message, e);
            throw new MKException(message);
        }
    }

    @PutMapping("userconfig")
    public void updateUserConfig(@Valid UserConfig userConfig) throws MKException {
        try {
            this.userConfigService.update(userConfig);
        } catch (Exception e) {
            message = "修改个性化配置失败";
            log.error(message, e);
            throw new MKException(message);
        }
    }

    @GetMapping("password/check")
    public boolean checkPassword(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password) {
        String encryptPassword = MD5Util.encrypt(username, password);
        User user = userService.findByName(username);
        if (user != null)
            return StringUtils.equals(user.getPassword(), encryptPassword);
        else
            return false;
    }

    @PutMapping("password")
    public void updatePassword(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password) throws MKException {
        try {
            userService.updatePassword(username, password);
        } catch (Exception e) {
            message = "修改密码失败";
            log.error(message, e);
            throw new MKException(message);
        }
    }

    @PutMapping("password/reset")
    @RequiresPermissions("user:reset")
    public void resetPassword(@NotBlank(message = "{required}") String usernames) throws MKException {
        try {
            String[] usernameArr = usernames.split(",");
            this.userService.resetPassword(usernameArr);
        } catch (Exception e) {
            message = "重置用户密码失败";
            log.error(message, e);
            throw new MKException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("user:export")
    public void export(User user, QueryRequest request, HttpServletResponse response) throws MKException {
        try {
            List<User> users = this.userService.findUserDetail(user, request);
            ExcelKit.$Export(User.class, response).downXlsx(users, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new MKException(message);
        }
    }
}
