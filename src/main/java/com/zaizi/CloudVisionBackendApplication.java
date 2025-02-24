package com.zaizi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.zaizi.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class CloudVisionBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudVisionBackendApplication.class, args);
    }

}
