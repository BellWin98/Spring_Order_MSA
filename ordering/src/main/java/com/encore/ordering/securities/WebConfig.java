package com.encore.ordering.securities;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    // 허용할 URL 및 메서드 정의
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8081") // vue의 url
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
