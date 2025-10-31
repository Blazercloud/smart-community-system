package com.neusoft.community.common.config;

import com.neusoft.community.common.interceptor.AuthInterceptor;
import com.neusoft.community.common.interceptor.AdminAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web全局配置类
 *
 * 主要职责：
 * 1️⃣ 注册用户与管理员拦截器
 * 2️⃣ 配置 CORS 跨域访问
 * 3️⃣ 配置 Swagger 静态资源访问路径
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor; // 用户拦截器

    @Autowired
    private AdminAuthInterceptor adminAuthInterceptor; // 管理员拦截器

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 普通用户接口拦截
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/user/**") // 用户端所有接口需要认证
                .excludePathPatterns(
                        "/user/login",
                        "/user/register"
                );

        // 管理员接口拦截
        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns("/admin/**") // 管理端接口
                .excludePathPatterns(
                        "/admin/login"
                );

        // Swagger / Knife4j 相关文档接口放行
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                );
    }

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * Swagger 资源配置
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Swagger UI 静态资源路径
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
    }
}
