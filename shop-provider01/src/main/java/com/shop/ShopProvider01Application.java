package com.shop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.shop.dao")
@EnableEurekaClient
@EnableFeignClients
@EnableTransactionManagement
public class ShopProvider01Application {
    public static void main(String[] args) {
        SpringApplication.run(ShopProvider01Application.class, args);
    }

}
