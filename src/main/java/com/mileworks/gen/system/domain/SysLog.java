package com.mileworks.gen.system.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.mileworks.gen.common.converter.TimeConverter;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_log")
@Excel("系统日志表")
public class SysLog implements Serializable {

    private static final long serialVersionUID = -8878596941954995444L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @TableField( "USERNAME")
    @ExcelField(value = "操作人")
    private String username;

    @TableField( "OPERATION")
    @ExcelField(value = "操作描述")
    private String operation;

    @TableField( "TIME")
    @ExcelField(value = "耗时（毫秒）")
    private Long time;

    @TableField( "METHOD")
    @ExcelField(value = "执行方法")
    private String method;

    @TableField( "PARAMS")
    @ExcelField(value = "方法参数")
    private String params;

    @TableField( "IP")
    @ExcelField(value = "IP地址")
    private String ip;

    @TableField( "CREATE_TIME")
    @ExcelField(value = "操作时间", writeConverter = TimeConverter.class)
    private Date createTime;

    @TableField(exist = false)
    private String createTimeFrom;
    @TableField(exist = false)
    private String createTimeTo;

    @TableField( "LOCATION")
    @ExcelField(value = "操作地点")
    private String location;

}