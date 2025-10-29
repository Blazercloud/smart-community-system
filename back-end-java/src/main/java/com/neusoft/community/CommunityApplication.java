

package com.neusoft.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 智慧社区平台启动类
 * 
 * @author Neusoft
 * @date 2024-10-27
 */
@SpringBootApplication
@MapperScan("com.neusoft.community.*.mapper")
public class CommunityApplication {

    private static final Logger logger = LoggerFactory.getLogger(CommunityApplication.class);

    public static void main(String[] args) {
        try {
            ConfigurableApplicationContext context = SpringApplication.run(CommunityApplication.class, args);
            logger.info("\n=================================");
            logger.info("东软智慧社区平台启动成功！");
            logger.info("API地址: http://localhost:8080/api");
            logger.info("=================================\n");
        } catch (Exception e) {
            logger.error("东软智慧社区平台启动失败", e);
        }
    }
}