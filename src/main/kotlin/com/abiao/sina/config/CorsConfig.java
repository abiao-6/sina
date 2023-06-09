package com.abiao.sina.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .maxAge(3600);
//        registry.addMapping("/**");
////                .allowCredentials(true)
////                .allowedMethods("GET", "POST", "HEAD", "PUT", "DELETE", "OPTIONS")
////                .allowedHeaders("*")
////                .allowedOrigins("*")
////                .maxAge(3600);
    }
}
