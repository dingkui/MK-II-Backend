package com.mileworks.gen.system.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.mileworks.gen.common.converter.TimeConverter;
import com.mileworks.gen.common.domain.RegexpConstant;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@TableName("t_user")
@Excel("用户信息表")
public class User implements Serializable {

    private static final long serialVersionUID = -4852732617765810959L;
    /**
     * 账户状态
     */
    public static final String STATUS_VALID = "1";

    public static final String STATUS_LOCK = "0";

    public static final String DEFAULT_AVATAR = "default.jpg";

    /**
     * 性别
     */
    public static final String SEX_MALE = "0";

    public static final String SEX_FEMALE = "1";

    public static final String SEX_UNKNOW = "2";

    // 默认密码
    public static final String DEFAULT_PASSWORD = "1234qwer";

    @TableId(value = "USER_ID", type = IdType.AUTO)
    private Long userId;

    @TableField("USERNAME")
    @Size(min = 4, max = 10, message = "{range}")
    @ExcelField(value = "用户名")
    private String username;

    @TableField("PASSWORD")
    private String password;

    @TableField("DEPT_ID")
    private Long deptId;

    @TableField(exist = false)
    @ExcelField(value = "部门")
    private String deptName;

    @TableField("EMAIL")
    @Size(max = 50, message = "{noMoreThan}")
    @Email(message = "{email}")
    @ExcelField(value = "邮箱")
    private String email;

    @TableField("MOBILE")
    @Pattern(regexp = RegexpConstant.MOBILE_REG, message = "{mobile}")
    @ExcelField(value = "手机号")
    private String mobile;

    @TableField("STATUS")
    @NotBlank(message = "{required}")
    @ExcelField(value = "状态", writeConverterExp = "0=锁定,1=有效")
    private String status;

    @TableField("CREATE_TIME")
    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

    @TableField("MODIFY_TIME")
    private Date modifyTime;

    @TableField("LAST_LOGIN_TIME")
    @ExcelField(value = "最后登录时间", writeConverter = TimeConverter.class)
    private Date lastLoginTime;

    @TableField("SSEX")
    @NotBlank(message = "{required}")
    @ExcelField(value = "性别", writeConverterExp = "0=男,1=女,2=保密")
    private String ssex;

    @TableField("DESCRIPTION")
    @Size(max = 100, message = "{noMoreThan}")
    @ExcelField(value = "个人描述")
    private String description;

    @TableField("AVATAR")
    private String avatar;

    @TableField(exist = false)
    @NotBlank(message = "{required}")
    private String roleId;
    @TableField(exist = false)
    @ExcelField(value = "角色")
    private String roleName;

    // 排序字段
    @TableField(exist = false)
    private String sortField;

    // 排序规则 ascend 升序 descend 降序
    @TableField(exist = false)
    private String sortOrder;

    @TableField(exist = false)
    private String createTimeFrom;
    @TableField(exist = false)
    private String createTimeTo;

    @TableField(exist = false)
    private String id;

    /**
     * shiro-redis v3.1.0 必须要有 getAuthCacheKey()或者 getId()方法
     * # Principal id field name. The field which you can get unique id to identify this principal.
     * # For example, if you use UserInfo as Principal class, the id field maybe userId, userName, email, etc.
     * # Remember to add getter to this id field. For example, getUserId(), getUserName(), getEmail(), etc.
     * # Default value is authCacheKey or id, that means your principal object has a method called "getAuthCacheKey()" or "getId()"
     *
     * @return userId as Principal id field name
     */
    public Long getAuthCacheKey() {
        return userId;
    }
}