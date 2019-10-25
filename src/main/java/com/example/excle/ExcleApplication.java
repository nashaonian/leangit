package com.example.excle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.excle.mapper")
public class ExcleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExcleApplication.class, args);
    }

}
