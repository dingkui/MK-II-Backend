package com.mileworks.gen.job.domain;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.mileworks.gen.common.converter.TimeConverter;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@TableName("t_job_log")
@Excel("调度日志信息表")
public class JobLog implements Serializable {

    private static final long serialVersionUID = -7114915445674333148L;

    @TableId(value = "LOG_ID", type = IdType.AUTO)
    private Long logId;

    private Long jobId;

    @ExcelField(value = "Bean名称")
    private String beanName;

    @ExcelField(value = "方法名称")
    private String methodName;

    @ExcelField(value = "方法参数")
    private String params;

    @ExcelField(value = "状态", writeConverterExp = "0=成功,1=失败")
    private String status;

    @ExcelField(value = "异常信息")
    private String error;

    @ExcelField(value = "耗时（毫秒）")
    private Long times;

    @ExcelField(value = "执行时间", writeConverter = TimeConverter.class)
    private Date createTime;

    @TableField(exist = false)
    private String createTimeFrom;

    @TableField(exist = false)
    private String createTimeTo;

}