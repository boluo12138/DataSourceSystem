package com.wzu.datasourcesystem.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class SqlSelect extends DatabaseInfo{
    @TableField(exist = false)
    private String sql;
}
