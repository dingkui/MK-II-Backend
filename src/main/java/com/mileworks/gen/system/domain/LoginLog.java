package com.mileworks.gen.system.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@TableName("t_login_log")
@ToString
@Data
public class LoginLog {
    /**
     * 用户 ID
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 登录时间
     */
    @TableField("LOGIN_TIME")
    private Date loginTime;

    /**
     * 登录地点
     */
    @TableField("LOCATION")
    private String location;

    @TableField("IP")
    private String ip;
}