package com.neusoft.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.neusoft.community.**.mapper") // ⭐ 自动扫描所有子包下的 mapper
public class CommunityApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
        System.out.println("=================================");
        System.out.println("东软智慧社区平台启动成功！");
        System.out.println("API地址: http://localhost:8989/api");
        System.out.println("=================================");
    }
}
