package com.wzu.datasourcesystem;

import com.wzu.datasourcesystem.mapper.DatabaseMapper;
import com.wzu.datasourcesystem.pojo.DatabaseInfo;
import com.wzu.datasourcesystem.utils.SQlUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.sql.DriverManager;
import java.sql.SQLException;

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
    public void testSQl(){
        DatabaseInfo databaseInfo = mapper.selectById(4);
        System.out.println("------------"+databaseInfo+" --------------------------------");
        boolean b = SQlUtils.isValid(databaseInfo);
        if(b){
            System.out.println("连接远程数据库成功");
        }else {
            System.out.println("连接远程数据库失败");

        }
    }
}