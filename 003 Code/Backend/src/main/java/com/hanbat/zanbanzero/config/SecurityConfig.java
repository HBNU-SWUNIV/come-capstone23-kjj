package com.hanbat.zanbanzero.config;

import com.hanbat.zanbanzero.auth.CustomAuthenticationManager;
import com.hanbat.zanbanzero.auth.login.filter.LonginFilter;
import com.hanbat.zanbanzero.auth.jwt.JwtAuthFilter;
import com.hanbat.zanbanzero.exception.filter.ExceptionHandlerBeforeBasicAuthentication;
import com.hanbat.zanbanzero.exception.filter.ExceptionHandlerBeforeUsernamePassword;
import com.hanbat.zanbanzero.repository.user.ManagerRepository;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    private final CustomAuthenticationManager customAuthenticationManager;
    private final CorsFilter corsFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter)
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilterBefore(new ExceptionHandlerBeforeUsernamePassword(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new LonginFilter(customAuthenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerBeforeBasicAuthentication(), BasicAuthenticationFilter.class)
                .addFilter(new JwtAuthFilter(customAuthenticationManager, userRepository, managerRepository))
                .authorizeHttpRequests()
                .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/manager/**").hasAnyRole("MANAGER", "ADMIN")
                .anyRequest().permitAll();

        return http.build();
    }
}
