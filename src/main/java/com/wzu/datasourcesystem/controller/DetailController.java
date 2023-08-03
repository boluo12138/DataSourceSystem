package com.wzu.datasourcesystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzu.datasourcesystem.common.R;
import com.wzu.datasourcesystem.mapper.DatabaseMapper;
import com.wzu.datasourcesystem.pojo.DatabaseInfo;
import com.wzu.datasourcesystem.pojo.Table;
import com.wzu.datasourcesystem.utils.DatabaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Transactional
@Slf4j
@RestController
@RequestMapping("/detail")
public class DetailController {
    @Autowired
    DatabaseMapper databaseMapper;

    @PostMapping("/database")
    public R<Set<DatabaseInfo>> getDatabase(@RequestBody DatabaseInfo database){
        log.info(database.toString());
        QueryWrapper<DatabaseInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("db_type",database.getDbType());
//                .select("db_name");
        List<DatabaseInfo> list = databaseMapper.selectList(wrapper);
        Set<DatabaseInfo> set = new HashSet<>(list);
        System.out.println("list.size--------: " +list.size());
        System.out.println("set.size---------: " +set.size());
        return R.success(set);
    }

    @PostMapping("/table")
    public R<List> getTable(@RequestBody DatabaseInfo database1){
//        QueryWrapper<DatabaseInfo> wrapper = new QueryWrapper<>();
//        wrapper.eq("db_type",database1.getDbType())
//                .eq("db_name",database1.getDbName());
//        DatabaseInfo database = databaseMapper.selectOne(wrapper);
        DatabaseInfo database = databaseMapper.selectById(database1.getId());
        if(database ==null){
            return R.error("查询失败");
        }
        System.out.println(database);
        DatabaseUtil.index(database);
        List<String> tableNames = DatabaseUtil.getTableNames();
        return R.success(tableNames);
    }

    @RequestMapping("/getAll")
    public R<ArrayList> getTableinfo(@RequestBody Table table){
        ArrayList<Object> list = new ArrayList<>();
        list.add(DatabaseUtil.getColumnComments(table.getTableName()));
        list.add(DatabaseUtil.getColumnNames(table.getTableName()));
        list.add(DatabaseUtil.getColumnTypes(table.getTableName()));
        list.add(DatabaseUtil.getColumnSizes(table.getTableName()));
        list.add(DatabaseUtil.getRow(table.getTableName()));
        return R.success(list);
    }

    @RequestMapping("/increment")
    public R<ArrayList<HashMap<String, String>>> increment(@RequestBody Table table){
        String sql;
        String columns = "";
        for (int i = 0; i < table.getColumns().size(); i++) {
            if(i==0){
                columns += table.getColumns().get(i);
            }else{
                columns += ","+table.getColumns().get(i);
            }
        }
        //SELECT id,stuNumber,password FROM st_login WHERE id BETWEEN 3 AND 8;
        sql = "SELECT " + columns + " FROM " + table.getTableName() + " WHERE " + table.getColumn() + " BETWEEN " + table.getBegin() +" AND " + table.getEnd() + ";";
        ArrayList<HashMap<String, String>> info = DatabaseUtil.getInfo(sql);
        return R.success(info);
    }

    @RequestMapping("/full")
    public R<ArrayList<HashMap<String, String>>> full(@RequestBody Table table){
        String sql;
        String columns = "";
        for (int i = 0; i < table.getColumns().size(); i++) {
            if(i==0){
                columns += table.getColumns().get(i);
            }else{
                columns += ","+table.getColumns().get(i);
            }
        }
        //SELECT id,stuNumber,password FROM st_login;
        sql = "SELECT " + columns + " FROM " + table.getTableName() + ";";
        System.out.println(sql);
        ArrayList<HashMap<String, String>> info = DatabaseUtil.getInfo(sql);
        return R.success(info);
    }
}
