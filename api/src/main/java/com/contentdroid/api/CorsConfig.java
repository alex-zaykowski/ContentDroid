package com.contentdroid.api;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                if (System.getenv("ENV") == null || !System.getenv("ENV").equals("PROD")){
                    registry
                            .addMapping("/**")
                            .allowedMethods(CorsConfiguration.ALL)
                            .allowedHeaders(CorsConfiguration.ALL)
                            .allowedOriginPatterns(CorsConfiguration.ALL);
                }
            }
        };
    }
}