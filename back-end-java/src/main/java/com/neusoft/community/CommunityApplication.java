package com.neusoft.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication(scanBasePackages = "com.neusoft.community")
@MapperScan("com.neusoft.community.**.mapper") // ⭐ 自动扫描所有子包下的 mapper
public class CommunityApplication {



    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
        System.out.println("=================================");
        System.out.println("东软智慧社区平台启动成功！");
        System.out.println("API地址: http://localhost:8989/api");
        System.out.println("=================================");
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许哪些域名访问
        corsConfiguration.addAllowedOrigin("*");
        // 允许哪些请求方式
        corsConfiguration.addAllowedMethod("*");
        // 允许哪些请求头
        corsConfiguration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

}
