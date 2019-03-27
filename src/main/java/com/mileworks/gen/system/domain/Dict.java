package com.mileworks.gen.system.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@ToString
@TableName("t_dict")
@Excel("字典信息表")
public class Dict implements Serializable {

    private static final long serialVersionUID = 7780820231535870010L;

    @TableId(value = "DICT_ID", type = IdType.AUTO)
    private Long dictId;

    @TableField("KEYY")
    @NotBlank(message = "{required}")
    @Size(max = 10, message = "{noMoreThan}")
    @ExcelField(value = "键")
    private String keyy;

    @TableField("VALUEE")
    @NotBlank(message = "{required}")
    @Size(max = 20, message = "{noMoreThan}")
    @ExcelField(value = "值")
    private String valuee;

    @TableField("TABLE_NAME")
    @NotBlank(message = "{required}")
    @Size(max = 20, message = "{noMoreThan}")
    @ExcelField(value = "表名")
    private String tableName;

    @TableField("FIELD_NAME")
    @NotBlank(message = "{required}")
    @Size(max = 20, message = "{noMoreThan}")
    @ExcelField(value = "字段名")
    private String fieldName;

}