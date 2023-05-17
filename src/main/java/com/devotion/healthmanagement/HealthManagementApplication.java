package com.devotion.healthmanagement;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@Slf4j
@SpringBootApplication
@MapperScan("com.devotion.healthmanagement.mapper")
public class HealthManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(HealthManagementApplication.class, args);
        log.info("项目启动成功");
    }

}
