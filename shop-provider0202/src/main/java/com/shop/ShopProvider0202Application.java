package com.shop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@MapperScan("com.shop.dao")
@EnableEurekaClient
public class ShopProvider0202Application {

    public static void main(String[] args) {
        SpringApplication.run(ShopProvider0202Application.class, args);
    }

}
