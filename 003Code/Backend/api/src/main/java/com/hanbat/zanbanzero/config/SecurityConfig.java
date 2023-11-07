package com.hanbat.zanbanzero.config;

import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.auth.jwt.filter.JwtAuthFilter;
import com.hanbat.zanbanzero.auth.jwt.filter.JwtRefreshFilter;
import com.hanbat.zanbanzero.auth.login.authentication_impl.AuthenticationManagerImpl;
import com.hanbat.zanbanzero.auth.login.filter.KeycloakLoginFilterV2;
import com.hanbat.zanbanzero.auth.login.filter.LoginFilterV2;
import com.hanbat.zanbanzero.auth.login.filter.util.CreateTokenInterfaceUserImpl;
import com.hanbat.zanbanzero.auth.monitor.IpCheckFilter;
import com.hanbat.zanbanzero.exception.handler.filter.ExceptionHandlerBeforeKeycloak;
import com.hanbat.zanbanzero.exception.handler.filter.ExceptionHandlerBeforeUsernamePassword;
import com.hanbat.zanbanzero.exception.handler.filter.template.ExceptionHandlerBeforeJwtAuth;
import com.hanbat.zanbanzero.external.KeycloakProperties;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import com.hanbat.zanbanzero.service.user.service.UserService;
import com.hanbat.zanbanzero.service.user.service.UserSsoService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${my.server.address}") private String address;
    @Value("${my.prometheus.path}") private String prometheusPath;
    @Value("${my.actuator.path}") private String actuatorPath;

    private final CorsFilter corsFilter;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final Keycloak keycloak;
    private final JwtTemplate jwtTemplate = new JwtTemplate();
    private final UserService userService;
    private final UserSsoService userSsoService;
    private final UserRepository userRepository;
    private final AuthenticationManagerImpl authenticationManager;
    private final KeycloakProperties properties;

    /**
     * Spring Security 설정을 무시하기 위한 빈
     *
     * @return WebSecurityCustomizer 빈
     */
    @Bean
    public WebSecurityCustomizer customizer() {
        return web -> web.ignoring().requestMatchers(
                "/apis",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/favicon.ico"
        );
    }

    /**
     * Spring Security 설정을 위한 빈
     *
     * @param http  HttpSecurity 객체
     * @return SecurityFilterChain 빈
     * @throws Exception http.sessionManagement()에서 throw 하는 예외
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter)
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilterBefore(new IpCheckFilter(prometheusPath, address), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerBeforeUsernamePassword(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new LoginFilterV2("/api/user/login/id", authenticationManager, new CreateTokenInterfaceUserImpl(), jwtUtil, jwtTemplate), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new LoginFilterV2("/api/manager/login/id", authenticationManager, new CreateTokenInterfaceUserImpl(), jwtUtil, jwtTemplate), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerBeforeKeycloak(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new KeycloakLoginFilterV2("/api/user/login/keycloak", restTemplate, properties, jwtUtil, jwtTemplate, userRepository, userSsoService, keycloak), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtRefreshFilter("/api/user/login/refresh", userService, jwtUtil, jwtTemplate), UsernamePasswordAuthenticationFilter.class)
                .addFilter(new JwtAuthFilter(authenticationManager, userRepository, jwtTemplate, jwtUtil))
                .addFilterBefore(new ExceptionHandlerBeforeJwtAuth(), JwtAuthFilter.class)
                .authorizeHttpRequests()
                .requestMatchers("/api/image").permitAll()
                .requestMatchers("/api/user/login/**").permitAll()
                .requestMatchers("/api/user/order/**/qr").permitAll()
                .requestMatchers("/api/manager/login/**").permitAll()
                .requestMatchers("/api/user/**").hasAnyRole("USER", "MANAGER")
                .requestMatchers("/api/manager/**").hasRole("MANAGER")
                .requestMatchers(prometheusPath).permitAll()
                .requestMatchers(actuatorPath + "health").permitAll()
                .anyRequest().authenticated();

        return http.build();
    }
}