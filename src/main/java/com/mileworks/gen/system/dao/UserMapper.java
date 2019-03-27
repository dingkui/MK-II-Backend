package com.mileworks.gen.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.mileworks.gen.system.domain.User;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

	List<User> findUserList(User user);

	List<User> findUserList(Pagination page, User user);
}