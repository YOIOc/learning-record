package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/* 使用@SpringBootApplication注解标注Web应用的启动类 */
@SpringBootApplication
public class BigEventApplication {
    public static void main( String[] args ) {
        SpringApplication.run(BigEventApplication.class, args);
    }
}
