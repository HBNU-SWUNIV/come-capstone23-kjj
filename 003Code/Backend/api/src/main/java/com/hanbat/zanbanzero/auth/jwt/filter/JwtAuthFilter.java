package com.hanbat.zanbanzero.auth.jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.entity.user.User;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterfaceImpl;
import com.hanbat.zanbanzero.exception.exceptions.JwtTokenException;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.*;

public class JwtAuthFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;
    private final JwtTemplate jwtTemplate;
    private final JwtUtil jwtUtil;

    public JwtAuthFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTemplate jwtTemplate, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtTemplate = jwtTemplate;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String jwtHeader = request.getHeader(jwtTemplate.getHeaderString());
//         JWT(Header)가 있는지 확인
        if ((jwtHeader == null || !jwtHeader.startsWith(jwtTemplate.getTokenPrefix()))) {
            chain.doFilter(request, response);
            return;
        }

        // JWT 검증
        String jwtToken = jwtHeader.replace(jwtTemplate.getTokenPrefix(), "");

        if (jwtUtil.isTokenExpired(jwtToken)) throw new JwtTokenException("토큰이 만료되었습니다.");
        String username = JWT.require(Algorithm.HMAC256(jwtTemplate.getSecret())).build().verify(jwtToken).getClaim("username").asString();
        String roles = JWT.require(Algorithm.HMAC256(jwtTemplate.getSecret())).build().verify(jwtToken).getClaim("roles").asString();

        String managerApiPrefix = "/api/manager";
        if (request.getRequestURI().startsWith(managerApiPrefix) && roles.equals("ROLE_USER")) throw new ServletException("권한 부족 (uri = " + request.getRequestURI() + ")");

        if (username != null) {
            User user = userRepository.findByUsername(username);
            UserDetailsInterface userDetails = new UserDetailsInterfaceImpl(user);

            // JWT 서명을 통해서 서명이 정상이면 Authentication 객체 만들어 줌
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

            // SecurityContextHolder = 시큐리티 세션 공간에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        }
    }
}
