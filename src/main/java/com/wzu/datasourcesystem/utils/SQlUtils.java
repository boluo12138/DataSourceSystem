package com.wzu.datasourcesystem.utils;

import com.wzu.datasourcesystem.pojo.DatabaseInfo;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SQlUtils {
    public static boolean isValid(DatabaseInfo databaseInfo) {
//        String url1 = "jdbc:mysql://127.0.0.1:3306/datasourcesystem?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&characterEncoding=utf-8";
//        String username = "root";
//        String password = "123456";
        String url = "jdbc:" + databaseInfo.getDbType() + "://"
                + databaseInfo.getDbIp() + ":" + databaseInfo.getPort() + "/"
                + databaseInfo.getDbName() +
                "?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&characterEncoding=utf-8";

        String username = databaseInfo.getUserName();
        String password = databaseInfo.getPassword();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(url, username, password);
//            System.out.println("连接远程数据库成功");
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("连接远程数据库失败");
            e.printStackTrace();
            return false;
        }
    }
}
