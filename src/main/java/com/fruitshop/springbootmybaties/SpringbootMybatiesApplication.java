package com.fruitshop.springbootmybaties;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;


@SpringBootApplication(scanBasePackages = {"com.fruitshop.springbootmybaties"})
@RestController
@MapperScan("com.fruitshop.springbootmybaties.dao")
public class SpringbootMybatiesApplication {

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        System.out.println("现在时间：" + sdf.format(date)); // 输出已经格式化的现在时间（24小时制）
        System.out.println( "Hello World!" );
        SpringApplication.run(SpringbootMybatiesApplication.class, args);
    }

}
