package com.abiao.sina;


import com.openaddr.ktCloud.KissKt;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan(basePackages = "com.abiao.sina.dao")
@SpringBootApplication()
public class SinaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SinaApplication.class, args);
        KissKt.getNamingService();
    }

}
