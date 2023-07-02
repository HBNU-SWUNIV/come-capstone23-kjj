package com.hanbat.zanbanzero.auth.login.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterfaceImpl;
import com.hanbat.zanbanzero.dto.user.LoginDto;
import com.hanbat.zanbanzero.entity.user.user.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KeycloakLoginFilter implements Filter {
    private String loginEndPath = "/login/keycloak";

    private final Environment environment;

    private String domain;
    private String realmName;
    private String clientId;
    private String grantType = "password";

    private final RestTemplate restTemplate;

    public KeycloakLoginFilter(Environment environment, RestTemplate restTemplate) {
        this.environment = environment;
        this.restTemplate = restTemplate;

        domain = environment.getProperty("my.keycloak.host");
        realmName = environment.getProperty("my.keycloak.realm_name");
        clientId = environment.getProperty("my.keycloak.client_id");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (((HttpServletRequest) request).getRequestURI().endsWith(loginEndPath)) {
            LoginDto dto = getUserInfo((HttpServletRequest) request);
            if (checkFromKeycloak(dto)) throw new AuthenticationServiceException("Keycloak 인증 실패");

            User newUser = User.of(dto);
            request.setAttribute("user", newUser);
            Authentication authentication = authentication(new UserDetailsInterfaceImpl(newUser));
            String accessToken = JwtUtil.createToken((UserDetailsInterface) authentication.getPrincipal());
            String refreshToken = JwtUtil.createRefreshToken((UserDetailsInterface) authentication.getPrincipal());

            ((HttpServletResponse)response).addHeader(JwtTemplate.HEADER_STRING, JwtTemplate.TOKEN_PREFIX + accessToken);
            ((HttpServletResponse)response).addHeader(JwtTemplate.REFRESH_HEADER_STRING, JwtTemplate.TOKEN_PREFIX + refreshToken);
        }
        chain.doFilter(request, response);
    }

    private LoginDto getUserInfo(HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(request.getInputStream(), LoginDto.class);
    }

    private boolean checkFromKeycloak(LoginDto dto) {
        String url = "http://" + domain + "/auth/realms/" + realmName + "/protocol/openid-connect/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("username", dto.getUsername());
        body.add("password", dto.getPassword());
        body.add("grant_type", grantType);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Object.class
        );
        return response.getStatusCode().isError();
    }

    private Authentication authentication(UserDetailsInterfaceImpl userDetails) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userDetails.getAuthorities().toString()));
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), authorities);
    }
}