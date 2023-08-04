package com.wzu.datasourcesystem.exception;

import com.alibaba.druid.pool.DruidDataSource;
import com.wzu.datasourcesystem.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

@RestControllerAdvice
//@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(SQLSyntaxErrorException.class)
    public void handleCustomException(SQLSyntaxErrorException ex) throws SQLException {
        // 构建JSON格式错误信息
//        String errorJson = "{\"status\": \"" + ex.getErrorCode()+ "\", \"message\": \"" + ex.getMessage() + "\"}";
//        return R.error(ex.toString());
        resetDataSource();
    }
    @Autowired
    DruidDataSource dataSource;
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
