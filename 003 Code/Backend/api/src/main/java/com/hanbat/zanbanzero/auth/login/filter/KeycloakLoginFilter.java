package com.hanbat.zanbanzero.auth.login.filter;

import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.auth.login.dao.KeycloakTokenDAO;
import com.hanbat.zanbanzero.auth.login.dao.KeycloakUserInfoDAO;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterfaceImpl;
import com.hanbat.zanbanzero.entity.user.user.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeycloakLoginFilter implements Filter {
    private String loginEndPath = "/login/keycloak";

    private String host;
    private String realmName;
    private String clientId;
    private String clientSecret;
    private String redirectUri;

    private final RestTemplate restTemplate;
    private final Environment environment;

    public KeycloakLoginFilter(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = restTemplate;
        this.environment = environment;

        host = environment.getProperty("my.keycloak.host");
        realmName = environment.getProperty("my.keycloak.realm_name");
        clientId = environment.getProperty("my.keycloak.client_id");
        clientSecret = environment.getProperty("my.keycloak.client_secret");
        redirectUri = environment.getProperty("my.keycloak.redirect_uri");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        if (servletRequest.getRequestURI().endsWith(loginEndPath)) {

            KeycloakUserInfoDAO userInfo = getUserInfoFromKeycloakServer(
                    getTokenFromKeycloakServer(servletRequest.getParameter("code")));

            User user;
            try {
                user = User.of(userInfo, checkUserInfo(userInfo));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            request.setAttribute("user", user);

            Authentication authentication = authentication(new UserDetailsInterfaceImpl(user));
            String accessToken = JwtUtil.createToken((UserDetailsInterface) authentication.getPrincipal());
            String refreshToken = JwtUtil.createRefreshToken((UserDetailsInterface) authentication.getPrincipal());

            ((HttpServletResponse)response).addHeader(JwtTemplate.HEADER_STRING, JwtTemplate.TOKEN_PREFIX + accessToken);
            ((HttpServletResponse)response).addHeader(JwtTemplate.REFRESH_HEADER_STRING, JwtTemplate.TOKEN_PREFIX + refreshToken);
        }
        chain.doFilter(request, response);
    }

    private String getTokenFromKeycloakServer(String code) {
        String url = host + "/auth/realms/" + realmName + "/protocol/openid-connect/token";
        String grantType = "authorization_code";
        String scope = "profile email roles openid";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("scope", scope);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<KeycloakTokenDAO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                KeycloakTokenDAO.class
        );
        return response.getBody().getAccess_token();
    }

    private KeycloakUserInfoDAO getUserInfoFromKeycloakServer(String token) {
        String url = host + "/auth/realms/" + realmName + "/protocol/openid-connect/userinfo";
        String tokenPrefix = "Bearer ";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", tokenPrefix + token);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(httpHeaders);

        ResponseEntity<KeycloakUserInfoDAO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                KeycloakUserInfoDAO.class
        );
        return response.getBody();
    }

    private String checkUserInfo(KeycloakUserInfoDAO dao) throws IllegalAccessException {
        String roleUser = "ROLE_USER";
        String roleManager = "ROLE_MANAGER";
        List<String> roles = Arrays.asList(dao.getRoles());
        if (roles.contains(roleManager)) return roleManager;
        else if (roles.contains(roleUser)) return roleUser;
        else throw new IllegalAccessException("need role");
    }

    private Authentication authentication(UserDetailsInterfaceImpl userDetails) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userDetails.getAuthorities().toString()));
        return new UsernamePasswordAuthenticationToken(userDetails, authorities);
    }
}