package com.hanbat.zanbanzero.auth.jwt.filter;

import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.exception.exceptions.JwtTokenException;
import com.hanbat.zanbanzero.service.user.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JwtAuthFilter extends BasicAuthenticationFilter {

    private final UserService userService;
    private final JwtTemplate jwtTemplate;
    private final JwtUtil jwtUtil;

    public JwtAuthFilter(AuthenticationManager authenticationManager, UserService userService, JwtTemplate jwtTemplate, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.userService = userService;
        this.jwtTemplate = jwtTemplate;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = request.getHeader(jwtTemplate.getHeaderString());
//         JWT(Header)가 있는지 확인
        if ((token == null || !token.startsWith(jwtTemplate.getTokenPrefix()))) {
            chain.doFilter(request, response);
            return;
        }

        // JWT 검증
        if (jwtUtil.isTokenExpired(token)) throw new JwtTokenException("토큰이 만료되었습니다.");
        String username = jwtUtil.getUsernameFromToken(token);
        String roles = jwtUtil.getRolesFromToken(token);

        String managerApiPrefix = "/api/manager";
        if (request.getRequestURI().startsWith(managerApiPrefix) && roles.equals("ROLE_USER")) throw new ServletException("권한 부족 (uri = " + request.getRequestURI() + ")");

        if (username != null) {
            UserDetailsInterface userDetails = userService.loadUserByUsername(username);

            // JWT 서명을 통해서 서명이 정상이면 Authentication 객체 만들어 줌
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

            // SecurityContextHolder = 시큐리티 세션 공간에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        }
    }
}
