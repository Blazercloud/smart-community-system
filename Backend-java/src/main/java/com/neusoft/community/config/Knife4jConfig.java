package com.neusoft.community.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("智慧社区平台API文档")
                        .version("1.0")
                        .description("东软智慧社区平台接口文档")
                        .contact(new Contact()
                                .name("Neusoft")
                                .email("support@neusoft.com")));
    }
}