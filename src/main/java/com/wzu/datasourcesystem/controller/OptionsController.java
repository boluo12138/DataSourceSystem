package com.wzu.datasourcesystem.controller;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.wzu.datasourcesystem.common.R;
import com.wzu.datasourcesystem.mapper.DatabaseMapper;
import com.wzu.datasourcesystem.mapper.OptionMapper;
import com.wzu.datasourcesystem.pojo.DatabaseInfo;
import com.wzu.datasourcesystem.pojo.SqlSelect;
import com.wzu.datasourcesystem.utils.SqlSelectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;

@Slf4j
@RequestMapping("/option")
@RestController
//@Transactional
public class OptionsController {
    @Autowired
    private OptionMapper mapper;

    //获取系统的名称和数据库名
    @Autowired
    DatabaseMapper databaseMapper;

    @PostMapping("/getSysName")
    public R<Set> getSysName(@RequestBody DatabaseInfo databaseInfo) {
        log.info(databaseInfo.toString());
        List<DatabaseInfo> list;
        QueryWrapper<DatabaseInfo> wrapper = new QueryWrapper<>();
        if (databaseInfo.getSysName().equals("")) {
            wrapper.select("sys_name")
                    .eq("db_type", databaseInfo.getDbType());
            list = databaseMapper.selectList(wrapper);
        } else if (databaseInfo.getDbName().equals("")) {

            wrapper.select("db_name", "id")
                    .eq("db_type", databaseInfo.getDbType())
                    .eq("sys_name", databaseInfo.getSysName());
            list = databaseMapper.selectList(wrapper);
        } else {
            return R.error("数据库选择错误");
        }
        Set<DatabaseInfo> sets = new HashSet<>(list);
        return R.success(sets);
    }


    @PostMapping("/select")
    public R<ArrayList> sqlselect(@RequestBody SqlSelect sqlSelect) {
//        log.info(sqlSelect.getDbName());
//        log.info(sqlSelect.toString());
        ArrayList<Object> result = new ArrayList<>();
        DatabaseInfo database = databaseMapper.selectById(sqlSelect.getId());
        if (database == null) {
            return R.error("数据库不存在");
        }
        String sql = sqlSelect.getSql().replaceAll("\n", " ").trim();
//        System.out.println("------------------");
//        System.out.println(sqlSelect.getSql());
//        System.out.println(sql);
        String[] sqls = sql.split(";");
//        System.out.println(sqls[0].toString());
//        System.out.println("------------------");
        try {
            updateDataSource(sqlSelect.getDbName());
            log.info(dataSource.getUrl().toString());
            for (int i = 0; i < sqls.length; i++) {
                if (sqls[i].toUpperCase().contains("SELECT") && sqls[i].toUpperCase().contains("FROM")) {
                    System.out.println("执行查询语句");
                    ArrayList data = SqlSelectUtil.select(database, sqls[i]);
                    result.add(data);
                } else if (sqls[i].toUpperCase().contains("UPDATE") && sqls[i].toUpperCase().contains("SET")) {
                    System.out.println("执行修改语句");
                    SqlRunner.db().update(sqls[i].trim());
                    result.add("修改成功");
                } else if (sqls[i].toUpperCase().contains("INSERT INTO")) {
                    System.out.println("执行插入语句");
                    SqlRunner.db().insert(sqls[i].trim());
                    result.add("插入成功");
                } else if (sqls[i].toUpperCase().contains("DELETE FROM")) {
                    System.out.println("执行删除语句");
                    SqlRunner.db().delete(sqls[i].trim());
                    result.add("删除成功");
                } else {
                    result.add("执行的语句输入有问题，请重新输入");
                }
            }
            resetDataSource();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return R.success(result);
    }

    // "select * from administrators;\n " +
//         "INSERT INTO `database_info` (`id`, `sys_name`, `db_type`, `db_ip`, `db_name`, `user_name`, `port`, `password`, `note`, `manage_id`) VALUES (7, 'human1', 'mysql', '127.0.0.1', 'human', 'root', '3306', '123456', '', 1);" +
//         "\n delete from administrators where uid=3;\n " +
//         "update administrators set username='zxb123456' where uid=2;"
    @Autowired
    DruidDataSource dataSource;

    public void updateDataSource(String dataName) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/" + dataName +
                "?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true";
        dataSource.close();
        dataSource.restart();
        dataSource.setUrl(url);
//        System.out.println("----------------------updateDataSource");
//        System.out.println(dataSource.getActiveCount());
//        System.out.println(dataSource.getUrl());
//        System.out.println(dataSource.getUsername());
//        System.out.println(dataSource.getPassword());
//        System.out.println(dataSource.getDbType());
//        System.out.println(dataSource.getDriver());
//        System.out.println("----------------------updateDataSource");
    }

    public void resetDataSource() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/datasourcesystem?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true";
        dataSource.close();
        dataSource.restart();
        dataSource.setUrl(url);
//        System.out.println("----------------------resetDataSource");
//        System.out.println(dataSource.getActiveCount());
//        System.out.println(dataSource.getUrl());
//        System.out.println(dataSource.getUsername());
//        System.out.println(dataSource.getPassword());
//        System.out.println(dataSource.getDbType());
//        System.out.println(dataSource.getDriver());
//        System.out.println("----------------------resetDataSource");
    }
}
