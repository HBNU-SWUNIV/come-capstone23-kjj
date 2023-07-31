package com.hanbat.zanbanzero.config;

import com.hanbat.zanbanzero.auth.login.authenticationManager.LoginAuthenticationManagerImpl;
import com.hanbat.zanbanzero.auth.jwt.filter.JwtAuthFilter;
import com.hanbat.zanbanzero.auth.jwt.filter.JwtRefreshFilter;
import com.hanbat.zanbanzero.auth.login.filter.KeycloakLoginFilter;
import com.hanbat.zanbanzero.auth.login.filter.LoginFilter;
import com.hanbat.zanbanzero.exception.filter.ExceptionHandlerBeforeKeycloak;
import com.hanbat.zanbanzero.exception.filter.ExceptionHandlerBeforeUsernamePassword;
import com.hanbat.zanbanzero.external.KeycloakProperties;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import com.hanbat.zanbanzero.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final RestTemplate restTemplate;
    private final UserService userService;
    private final UserRepository userRepository;
    private final LoginAuthenticationManagerImpl authenticationManager;
    private final KeycloakProperties properties;

    @Bean
    public WebSecurityCustomizer customizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/apis",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/favicon.ico"
        );
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter)
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilterBefore(new ExceptionHandlerBeforeUsernamePassword(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new LoginFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerBeforeKeycloak(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new KeycloakLoginFilter("/api/user/login/keycloak", restTemplate, properties), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtRefreshFilter(userService), UsernamePasswordAuthenticationFilter.class)
                .addFilter(new JwtAuthFilter(authenticationManager, userRepository))
                .authorizeHttpRequests()
                .requestMatchers("/api/image").permitAll()
                .requestMatchers("/api/user/login/**").permitAll()
                .requestMatchers("/api/manager/login/**").permitAll()
                .requestMatchers("/api/user/**").hasAnyRole("USER", "MANAGER")
                .requestMatchers("/api/manager/**").hasAnyRole("MANAGER")
                .anyRequest().authenticated();

        return http.build();
    }
}