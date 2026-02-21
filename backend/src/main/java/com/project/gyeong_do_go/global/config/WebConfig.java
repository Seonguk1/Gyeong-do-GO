package com.project.gyeong_do_go.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
                .allowedOrigins("*") // 모든 출처 허용 (보안상 개발 단계에서만 추천)
                .allowedMethods("*") // 허용할 HTTP 메서드
                .allowedHeaders("*"); // 모든 헤더 허용
    }
}