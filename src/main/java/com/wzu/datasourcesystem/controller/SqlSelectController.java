package com.wzu.datasourcesystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzu.datasourcesystem.common.R;
import com.wzu.datasourcesystem.mapper.DatabaseMapper;
import com.wzu.datasourcesystem.pojo.DatabaseInfo;
import com.wzu.datasourcesystem.pojo.SqlSelect;
import com.wzu.datasourcesystem.utils.SqlSelectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Slf4j
@RestController
@RequestMapping("/sql")
public class SqlSelectController {

    @Autowired
    DatabaseMapper databaseMapper;

    @PostMapping("/getSysName")
    public R<List> getSysName(@RequestBody DatabaseInfo databaseInfo){
        log.info(databaseInfo.toString());
        List<DatabaseInfo> list;
        QueryWrapper<DatabaseInfo> wrapper = new QueryWrapper<>();
        if( databaseInfo.getSysName().equals("")){
            wrapper.select("sys_name")
                    .eq("db_type",databaseInfo.getDbType());
            list = databaseMapper.selectList(wrapper);
        }else if(databaseInfo.getDbName().equals("")){
            wrapper.select("db_name")
                    .eq("db_type",databaseInfo.getDbType())
                    .eq("sys_name",databaseInfo.getSysName());
            list = databaseMapper.selectList(wrapper);
        }else{
            return R.error("数据库选择错误");
        }
        return R.success(list);
    }



    @RequestMapping("/select")
    public R<ArrayList> sqlselect(@RequestBody SqlSelect sqlSelect){//Database database, @RequestParam("sql") String sql
        ArrayList<Object> result = new ArrayList<>();
        QueryWrapper<DatabaseInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("sys_name",sqlSelect.getSysName())
                .eq("db_type",sqlSelect.getDbType())
                .eq("db_name",sqlSelect.getDbName());
        DatabaseInfo database = databaseMapper.selectOne(wrapper);
        if(database == null){
            return R.error("数据库不存在");
        }

        String[] sqls = sqlSelect.getSql().split(";");
        for (int i = 0; i < sqls.length; i++) {
            ArrayList data = SqlSelectUtil.select(database,sqls[i]);
            if(data!=null){
                result.add(data);
            }
            else {
                result.add(R.error("sql语句查询失败"));
            }
        }
        return R.success(result);
    }
}
