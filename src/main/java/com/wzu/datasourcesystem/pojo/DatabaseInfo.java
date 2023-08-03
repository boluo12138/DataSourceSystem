package com.wzu.datasourcesystem.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@TableName("database_info")
public class DatabaseInfo {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String sysName;
    private String dbType;
    private String dbIp;
    private String dbName;
    private String userName;
    private String port;
    private String password;
    private String note;
    private Integer manageId;
    @TableField(exist = false)
    private Integer pageNum;
    @TableField(exist = false)
    private Integer pageSize;
//    @TableField(exist = false)
//    private Integer[] ids;
//    @TableField(exist = false)
//    private String[] options;
}
