package com.wzu.datasourcesystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzu.datasourcesystem.common.R;
import com.wzu.datasourcesystem.mapper.DatabaseMapper;
import com.wzu.datasourcesystem.pojo.DatabaseInfo;
import com.wzu.datasourcesystem.utils.SQlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;

@Transactional
@Slf4j
@RequestMapping("/database")
@RestController
@CrossOrigin
public class DatabaseController {
    @Autowired
    private DatabaseMapper databaseMapper;

    //    @RequestMapping("/getAll")
    @PostMapping("/getAll")
    public R<Page> getAll(@RequestBody DatabaseInfo databaseInfo) {
//        log.info(page + " " + pageSize);
//        page=1;
        log.info(databaseInfo.toString());
        int pageNum = databaseInfo.getPageNum();
        int pageSize = databaseInfo.getPageSize();
        Page<DatabaseInfo> pageData = new Page<>(pageNum, pageSize);
        QueryWrapper<DatabaseInfo> wrapper = new QueryWrapper<>();
        log.info(databaseInfo.toString());
        if (databaseInfo.getSysName() != null && !databaseInfo.getSysName().equals("")) {
            wrapper.like("sys_name", databaseInfo.getSysName());
        }
        if (databaseInfo.getDbName() != null && !databaseInfo.getDbName().equals("")) {
            wrapper.like("db_name", databaseInfo.getDbName());
        }
        if (!databaseInfo.getDbType().equals("查询全部")) {
            wrapper.eq("db_type", databaseInfo.getDbType());
        }
        System.out.println(wrapper.getSqlSelect());
        Page<DatabaseInfo> databaseInfoPage = databaseMapper.selectPage(pageData, wrapper);
//        List<DatabaseInfo> list = databaseMapper.selectList(null);
        return R.success(databaseInfoPage);
    }

    @PostMapping("/add")
    public R<DatabaseInfo> add(@RequestBody DatabaseInfo databaseInfo) {
        boolean isCreate = SQlUtils.isValid(databaseInfo);
        log.info(databaseInfo.toString());
        if (isCreate) {
            databaseMapper.insert(databaseInfo);
            return R.success(databaseInfo);
        } else {
            return R.error("连接失败，无法插入，请重新检查数据源");
        }

    }

    @PostMapping("/update")
    public R<DatabaseInfo> update(@RequestBody DatabaseInfo databaseInfo) {
        boolean isCreate = SQlUtils.isValid(databaseInfo);
        if (isCreate) {
            databaseMapper.updateById(databaseInfo);
            log.info(databaseInfo.toString());
            return R.success(databaseInfo);
        } else {
            return R.error("连接失败，无法修改，请重新检查数据源");
        }
    }

    @PostMapping("/test")
    public R<String> testSQL(@RequestBody DatabaseInfo databaseInfo) {
        System.out.println("------------" + databaseInfo + " --------------------------------");
        boolean b = SQlUtils.isValid(databaseInfo);
        if (b) {
//            System.out.println("连接远程数据库成功");
            return R.success("连接远程数据库成功");
        } else {
//            System.out.println("连接远程数据库失败");
            return R.error("连接远程数据库失败");

        }
    }

    @PostMapping("/delete")
    public R<String> deleteSQL(@RequestBody DatabaseInfo databaseInfo) {
        int i = databaseMapper.deleteBatchIds(Arrays.asList(databaseInfo.getIds()));
//        int i = databaseMapper.deleteById(id);
        if(i>0){
            return R.success("删除成功");
        }else {
            return R.error("删除失败");
        }
    }
}
