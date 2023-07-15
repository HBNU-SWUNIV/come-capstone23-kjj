package com.hanbat.zanbanzero.auth.jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.entity.user.user.User;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterfaceImpl;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.*;
import java.util.Arrays;

public class JwtAuthFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    private String userApiPrefix = "/api/user";
    private String managerApiPrefix = "/api/manager";
    private String[] passPath = { "/swagger-ui/index.html",
            userApiPrefix + "/join",
            userApiPrefix + "/join/check",
            userApiPrefix + "/login/id",
            managerApiPrefix + "/login/id",
            userApiPrefix + "/login/keycloak",
            userApiPrefix + "/login/keycloak/redirect",
            userApiPrefix + "/login/keycloak/page",
            userApiPrefix + "/login/refresh"};

    public JwtAuthFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String jwtHeader = request.getHeader(JwtTemplate.HEADER_STRING);
        // JWT(Header)가 있는지 확인
        if ((jwtHeader == null || !jwtHeader.startsWith(JwtTemplate.TOKEN_PREFIX))) {
            if (Arrays.asList(passPath).contains(request.getRequestURI())){
                chain.doFilter(request, response);
                return;
            }
            throw new ServletException("토큰이 없거나 잘못되었습니다.");
        }

        // JWT 검증
        String jwtToken = jwtHeader.replace(JwtTemplate.TOKEN_PREFIX, "");

        String username = JWT.require(Algorithm.HMAC256(JwtTemplate.SECRET)).build().verify(jwtToken).getClaim("username").asString();
        String roles = JWT.require(Algorithm.HMAC256(JwtTemplate.SECRET)).build().verify(jwtToken).getClaim("roles").asString();

        if (request.getRequestURI().startsWith(managerApiPrefix) && roles.equals("ROLE_USER")) throw new ServletException("권한 부족");

        if (username != null) {
            User user = userRepository.findByUsername(username);
            UserDetailsInterface userDetails = new UserDetailsInterfaceImpl(user);

            // JWT 서명을 통해서 서명이 정상이면 Authentication 객체 만들어 줌
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // SecurityContextHolder = 시큐리티 세션 공간에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        }
        else throw new ServletException("username is null");
    }
}
