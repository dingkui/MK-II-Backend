package com.mileworks.gen.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.mileworks.gen.system.domain.LoginLog;

public interface LoginLogService extends IService<LoginLog> {

    void saveLoginLog(LoginLog loginLog);
}
