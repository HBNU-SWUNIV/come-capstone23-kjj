package com.hanbat.zanbanzero.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.entity.user.manager.Manager;
import com.hanbat.zanbanzero.entity.user.user.User;
import com.hanbat.zanbanzero.auth.login.userDetails.ManagerDetailsInterfaceImpl;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterfaceImpl;
import com.hanbat.zanbanzero.repository.user.ManagerRepository;
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

    private UserRepository userRepository;
    private ManagerRepository managerRepository;


    public JwtAuthFilter(AuthenticationManager authenticationManager, UserRepository userRepository, ManagerRepository managerRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtHeader = request.getHeader(JwtTemplate.HEADER_STRING);
        // JWT(Header)가 있는지 확인
        if ((jwtHeader == null || !jwtHeader.startsWith(JwtTemplate.TOKEN_PREFIX))) {
            if (request.getRequestURI().startsWith("/join") || request.getRequestURI().startsWith("/login/")){
                chain.doFilter(request, response);
                return;
            }
            throw new IOException("토큰이 없거나 잘못되었습니다.");
        }

        // JWT 검증
        String jwtToken = jwtHeader.replace(JwtTemplate.TOKEN_PREFIX, "");

        String username = JWT.require(Algorithm.HMAC256(JwtTemplate.SECRET)).build().verify(jwtToken).getClaim("username").asString();
        String roles = JWT.require(Algorithm.HMAC256(JwtTemplate.SECRET)).build().verify(jwtToken).getClaim("roles").asString();

        if (username != null) {
            UserDetailsInterface userDetails = null;
            if (roles.equals("ROLE_USER")) {
                User user = userRepository.findByUsername(username);
                userDetails = new UserDetailsInterfaceImpl(user);
            }
            else if (roles.equals("ROLE_MANAGER")) {
                Manager manager = managerRepository.findByUsername(username);
                userDetails = new ManagerDetailsInterfaceImpl(manager);
            }

            // JWT 서명을 통해서 서명이 정상이면 Authentication 객체 만들어 줌
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // SecurityContextHolder = 시큐리티 세션 공간에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        }
    }
}
