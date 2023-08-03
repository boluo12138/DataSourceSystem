package com.wzu.datasourcesystem.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@TableName(value = "administrators")
public class Users {
    private Integer uid;
    private String username;
    private String password;
    private String email;
    @TableField(exist = false)
    private String code;
}
