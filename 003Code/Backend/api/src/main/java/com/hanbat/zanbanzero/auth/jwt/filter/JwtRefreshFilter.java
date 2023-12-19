package com.hanbat.zanbanzero.auth.jwt.filter;


import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import com.hanbat.zanbanzero.auth.util.JwtUtil;
import com.hanbat.zanbanzero.auth.login.user_details.UserDetailsInterface;
import com.hanbat.zanbanzero.auth.util.RedisAuthUtil;
import com.hanbat.zanbanzero.service.user.user.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JwtRefreshFilter implements Filter {

    private final UserService service;
    private final JwtUtil jwtUtil;
    private final JwtTemplate jwtTemplate;
    private final String uri;
    private final RedisAuthUtil redisAuthUtil;

    public JwtRefreshFilter(String uri, UserService service, JwtUtil jwtUtil, JwtTemplate jwtTemplate, RedisAuthUtil redisAuthUtil) {
        this.uri = uri;
        this.service = service;
        this.jwtTemplate = jwtTemplate;
        this.jwtUtil = jwtUtil;
        this.redisAuthUtil = redisAuthUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        if (!(servletRequest.getRequestURI().equals(uri))) chain.doFilter(request, response);
        else {
            String refreshToken = servletRequest.getHeader(jwtTemplate.getHeaderString());
            String username = jwtUtil.getUsernameFromRefreshToken(refreshToken);
            if (refreshToken == null) servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            else if (jwtUtil.isTokenExpired(refreshToken) ||
                    !jwtUtil.getTypeFromRefreshToken(refreshToken).equals(jwtTemplate.getRefreshType()) ||
                    !redisAuthUtil.matchRefreshToken(username, refreshToken)) servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            else {
                UserDetailsInterface userDetails = service.loadUserByUsername(username);

                String jwtToken = jwtUtil.createToken(userDetails);
                String newRefreshToken = jwtUtil.createRefreshToken(userDetails);

                servletResponse.setStatus(HttpServletResponse.SC_OK);
                jwtUtil.setTokenToResponseHeader(servletResponse, jwtToken, newRefreshToken);
                redisAuthUtil.setRefreshTokenDataToRedis(username, newRefreshToken);

                chain.doFilter(request, response);
            }
        }
    }
}
