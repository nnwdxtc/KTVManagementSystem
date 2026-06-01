package com.ktv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ktv")
@MapperScan("com.ktv.dao")
public class KtvApplication {
    public static void main(String[] args) {
        SpringApplication.run(KtvApplication.class, args);
    }
}