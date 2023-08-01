package com.wzu.datasourcesystem.utils;

import com.wzu.datasourcesystem.pojo.DatabaseInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SqlSelectUtil {

    private static String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
//    private static String URL = "jdbc:mysql://127.0.0.1:3306/student?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&characterEncoding=utf-8";
//    private static String USERNAME = "root";
//    private static String PASSWORD = "hyj.0223";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static ArrayList<HashMap<String,String>> select(DatabaseInfo database, String sql) {

        URL = "jdbc:"+database.getDbType()+"://"+database.getDbIp()+":"+database.getPort()+"/"+database.getDbName()+"?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&characterEncoding=utf-8";
        USERNAME = database.getUserName();
        PASSWORD = database.getPassword();
        Connection conn = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
//            for (int i = 0; i < metaData.getColumnCount(); i++) {
//                list.add(metaData.getColumnName(i+1));
//            }
            while (resultSet.next()){
                HashMap<String,String> data = new HashMap();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
//                    list.add(resultSet.getString(i+1));
                    data.put(metaData.getColumnName(i),resultSet.getString(i));

                }
//                System.out.println(data);
                list.add(data);
            }
            resultSet.close();
            statement.close();
            conn.close();
            return list;

        } catch (SQLException e) {
//            throw new RuntimeException(e);
            return null;
        }

    }
}
