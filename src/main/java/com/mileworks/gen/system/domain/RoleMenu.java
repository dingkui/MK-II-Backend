package com.mileworks.gen.system.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName("t_role_menu")
@Data
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = -7573904024872252113L;

    @TableField("ROLE_ID")
    private Long roleId;

    @TableField("MENU_ID")
    private Long menuId;
}