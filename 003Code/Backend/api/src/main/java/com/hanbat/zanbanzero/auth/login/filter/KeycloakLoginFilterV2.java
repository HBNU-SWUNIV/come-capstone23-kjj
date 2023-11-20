package com.hanbat.zanbanzero.auth.login.filter;

import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.auth.login.dto.KeycloakUserInfoDto;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterfaceImpl;
import com.hanbat.zanbanzero.entity.user.User;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByUsernameException;
import com.hanbat.zanbanzero.exception.exceptions.KeycloakLoginException;
import com.hanbat.zanbanzero.external.KeycloakProperties;
import com.hanbat.zanbanzero.service.user.sso.UserSsoService;
import com.hanbat.zanbanzero.service.user.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class KeycloakLoginFilterV2 extends AbstractAuthenticationProcessingFilter {

    private final KeycloakProperties properties;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final JwtTemplate jwtTemplate;
    private final UserService userService;
    private final UserSsoService userSsoService;
    private final Keycloak keycloak;

    public KeycloakLoginFilterV2(String filterProcessesUrl, RestTemplate restTemplate, KeycloakProperties properties, JwtUtil jwtUtil, JwtTemplate jwtTemplate, UserService userService, UserSsoService userSsoService, Keycloak keycloak) {
        super(filterProcessesUrl);
        this.restTemplate = restTemplate;
        this.properties = properties;
        this.jwtUtil = jwtUtil;
        this.jwtTemplate = jwtTemplate;
        this.userService = userService;
        this.userSsoService = userSsoService;
        this.keycloak = keycloak;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        KeycloakUserInfoDto userInfo = getUserInfoFromKeycloakServer(request.getParameter("token"));
        String sub = userInfo.getSub();
        if (sub == null) throw new KeycloakLoginException("keycloak user sub is null");

        String userSub = sub + "_keycloak";
        User user;
        try {
            user = userService.findByUsername(userSub);
        } catch (CantFindByUsernameException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
        if (user == null) user = userSsoService.join(User.of(userSub, checkUserInfo(userInfo)));
        request.setAttribute("user", user);

        UserDetailsInterface userDetails = new UserDetailsInterfaceImpl(user);
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String accessToken = jwtUtil.createToken((UserDetailsInterface) authResult.getPrincipal());
        String refreshToken = jwtUtil.createRefreshToken((UserDetailsInterface) authResult.getPrincipal());

        response.addHeader(jwtTemplate.getHeaderString(), jwtTemplate.getTokenPrefix() + accessToken);
        response.addHeader(jwtTemplate.getRefreshHeaderString(), jwtTemplate.getTokenPrefix() + refreshToken);

        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        throw new ServletException("keycloak login failed");
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
        KeycloakUserInfoDto body = response.getBody();
        if (body == null) throw new KeycloakLoginException("keycloak userInfo is null");
        keycloak.tokenManager().invalidate(token);
        return body;
    }

    private String checkUserInfo(KeycloakUserInfoDto dao) throws KeycloakLoginException {
        String roleUser = "ROLE_USER";
        String roleManager = "ROLE_MANAGER";
        List<String> roles = Arrays.asList(dao.getRoles());
        if (roles.contains(roleManager)) return roleManager;
        else if (roles.contains(roleUser)) return roleUser;
        else throw new KeycloakLoginException("need roles : " + roles);
    }
}