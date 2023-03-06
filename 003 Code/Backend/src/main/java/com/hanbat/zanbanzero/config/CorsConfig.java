package com.hanbat.zanbanzero.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 내 서버가 응답을 할 때 json을 js에서 처리할 수 있게 할지를 설정
        config.addAllowedOrigin("*"); // 모든 ip(*)에 응답을 허용
        config.addAllowedHeader("*"); // 모든 헤더(*)에 응답을 허용
        config.addAllowedMethod("*"); // 모든 method(post, get, put, delete, patch) 허용
        source.registerCorsConfiguration("/api/**", config); // /api/* 주소에 대해 config 적용
        return new CorsFilter(source);
    }
}
