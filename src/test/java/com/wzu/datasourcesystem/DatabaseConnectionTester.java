package com.wzu.datasourcesystem;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.wzu.datasourcesystem.mapper.DatabaseMapper;
import com.wzu.datasourcesystem.mapper.OptionMapper;
import com.wzu.datasourcesystem.pojo.DatabaseInfo;
import com.wzu.datasourcesystem.utils.SQlUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class DatabaseConnectionTester {

    @Test
    public void testRemoteJdbc() {
        String url = "jdbc:mysql://127.0.0.1:3306/datasourcesystem?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(url, username, password);
            System.out.println("连接远程数据库成功");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("连接远程数据库失败");
            e.printStackTrace();
        }
    }

    @Autowired
    DatabaseMapper mapper;

    @Test
    public void testSQl() {
        DatabaseInfo databaseInfo = mapper.selectById(4);
        System.out.println("------------" + databaseInfo + " --------------------------------");
        boolean b = SQlUtils.isValid(databaseInfo);
        if (b) {
            System.out.println("连接远程数据库成功");
        } else {
            System.out.println("连接远程数据库失败");

        }
    }

    @Autowired
    OptionMapper optionMapper;

    @Test
    public void testOption() {
        String option = "select * FROM database_info";
        List<Map<String, Object>> maps = SqlRunner.db().selectList(option);
        maps.forEach(System.out::println);
//        QueryWrapper wrapper = new QueryWrapper<>();
//        wrapper.select(option);
//        List<String> list = mapper.selectList(wrapper);
//        System.out.println(list);
    }

    @Autowired
    DruidDataSource dsa;

    @Test
    public void testDruid() throws SQLException {
        System.out.println();
        dsa.setAsyncInit(false);
//        System.out.println(dataSource.getPassword());
//        System.out.println(dataSource.getDriverClasName());
        String dataName = "shixi";
        String url = "jdbc:mysql://localhost:3306/" + dataName +
                "?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true";
        System.out.println(url);
        dsa.restart();
        dsa.setUrl(url);
        String option = "select * FROM t_user";
        System.out.println("----------------SQL语句执行如下——————————————");
        System.out.println(option);
        List<Map<String, Object>> maps = SqlRunner.db().selectList(option);

        maps.forEach(System.out::println);
        System.out.println("----------------SQL语句执行如上——————————————");
//        System.out.println(dsa.getUrl());
        System.out.println();

    }
}

