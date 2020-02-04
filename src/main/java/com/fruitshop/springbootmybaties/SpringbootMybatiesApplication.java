package com.fruitshop.springbootmybaties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SpringbootMybatiesApplication {

    public static void main(String[] args) {
        System.out.println( "Hello World!" );
        SpringApplication.run(SpringbootMybatiesApplication.class, args);
    }

}
