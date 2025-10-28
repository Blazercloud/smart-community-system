package com.neusoft.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 智慧社区平台启动类
 * 
 * @author Neusoft
 * @date 2024-10-27
 */
@SpringBootApplication
@MapperScan("com.neusoft.community.*.mapper")
public class CommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
        System.out.println("\n=================================");
        System.out.println("东软智慧社区平台启动成功！");
        System.out.println("API地址: http://localhost:8080/api");
        System.out.println("=================================\n");
    }
}

