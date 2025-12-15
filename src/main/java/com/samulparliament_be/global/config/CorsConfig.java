package com.samulparliament_be.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // 프론트엔드 도메인 허용
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",          // 로컬 개발
                "https://samulparliament.com"     // 실제 프론트
        ));

        // 허용할 HTTP 메서드
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        // 허용할 헤더
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type"
        ));

        // Authorization 헤더(JWT) 허용
        config.setAllowCredentials(true);

        // 모든 경로에 적용
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}