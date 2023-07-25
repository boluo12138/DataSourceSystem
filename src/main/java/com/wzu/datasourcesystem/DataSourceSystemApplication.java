package com.wzu.datasourcesystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@MapperScan("com.wzu.datasourcesystem.mapper")
@SpringBootApplication
public class DataSourceSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataSourceSystemApplication.class, args);
    }

}
