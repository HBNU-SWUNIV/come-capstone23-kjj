package com.hanbat.zanbanzero.auth.login.filter;

import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.auth.login.dto.KeycloakTokenDto;
import com.hanbat.zanbanzero.auth.login.dto.KeycloakUserInfoDto;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterfaceImpl;
import com.hanbat.zanbanzero.entity.user.user.User;
import com.hanbat.zanbanzero.external.KeycloakProperties;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class KeycloakLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final KeycloakProperties properties;
    private final RestTemplate restTemplate;

    public KeycloakLoginFilter(String filterProcessesUrl, RestTemplate restTemplate, KeycloakProperties properties) {
        super(filterProcessesUrl);
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        KeycloakUserInfoDto userInfo = getUserInfoFromKeycloakServer(request.getParameter("token"));
        User user;
        try {
            user = User.of(userInfo, checkUserInfo(userInfo));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("user", user);

        UserDetailsInterfaceImpl userDetails = new UserDetailsInterfaceImpl(user);
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String accessToken = JwtUtil.createToken((UserDetailsInterface) authResult.getPrincipal());
        String refreshToken = JwtUtil.createRefreshToken((UserDetailsInterface) authResult.getPrincipal());

        response.addHeader(JwtTemplate.HEADER_STRING, JwtTemplate.TOKEN_PREFIX + accessToken);
        response.addHeader(JwtTemplate.REFRESH_HEADER_STRING, JwtTemplate.TOKEN_PREFIX + refreshToken);

        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        throw new ServletException("keycloak login failed");
    }

    private String getTokenFromKeycloakServer(String code) {
        String url = properties.getHost() + "/auth/realms/" + properties.getRealmName() + "/protocol/openid-connect/token";
        String grantType = "authorization_code";
        String scope = "profile email roles openid";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grantType);
        body.add("client_id", properties.getClientId());
        body.add("client_secret", properties.getClientSecret());
        body.add("redirect_uri", properties.getRedirectUri());
        body.add("code", code);
        body.add("scope", scope);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<KeycloakTokenDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                KeycloakTokenDto.class
        );
        return response.getBody().getAccess_token();
    }

    private KeycloakUserInfoDto getUserInfoFromKeycloakServer(String token) {
        String url = properties.getHost() + "/auth/realms/" + properties.getRealmName() + "/protocol/openid-connect/userinfo";
        String tokenPrefix = "Bearer ";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", tokenPrefix + token);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(httpHeaders);

        ResponseEntity<KeycloakUserInfoDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                KeycloakUserInfoDto.class
        );
        return response.getBody();
    }

    private String checkUserInfo(KeycloakUserInfoDto dao) throws IllegalAccessException {
        String roleUser = "ROLE_USER";
        String roleManager = "ROLE_MANAGER";
        List<String> roles = Arrays.asList(dao.getRoles());
        if (roles.contains(roleManager)) return roleManager;
        else if (roles.contains(roleUser)) return roleUser;
        else throw new IllegalAccessException("need role");
    }
}