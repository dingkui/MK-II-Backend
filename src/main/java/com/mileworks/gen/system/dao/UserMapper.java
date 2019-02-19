package com.mileworks.gen.system.dao;

import com.mileworks.gen.common.config.MyMapper;
import com.mileworks.gen.system.domain.User;

import java.util.List;

public interface UserMapper extends MyMapper<User> {

	List<User> findUserDetail(User user);
}