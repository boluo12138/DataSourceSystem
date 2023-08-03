package com.wzu.datasourcesystem.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Table extends DatabaseInfo{
    private String tableName;

    private String column;
    private Integer begin;
    private Integer end;
    private List<String> Columns;

    private String filtration;
}
