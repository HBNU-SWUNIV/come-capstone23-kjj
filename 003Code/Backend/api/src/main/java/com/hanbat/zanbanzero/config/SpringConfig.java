package com.hanbat.zanbanzero.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SpringConfig {

    /**
     * 비밀번호 암호화 및 검증을 위한 Encoder
     * 
     * @return BCryptPasswordEncoder 빈
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * HTTP 통신을 위한 RestTemplate
     * 
     * @return RestTemplate 빈
     */
    @Bean
    public RestTemplate restTemplate() { return new RestTemplate(); }
}
