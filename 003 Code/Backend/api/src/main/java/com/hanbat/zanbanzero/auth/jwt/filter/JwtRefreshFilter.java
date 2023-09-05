package com.hanbat.zanbanzero.auth.jwt.filter;


import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.service.user.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JwtRefreshFilter implements Filter {

    private final UserService service;
    private final JwtUtil jwtUtil;
    private final JwtTemplate jwtTemplate;

    public JwtRefreshFilter(UserService service, JwtUtil jwtUtil, JwtTemplate jwtTemplate) {
        this.service = service;
        this.jwtTemplate = jwtTemplate;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        String refreshUri = "/api/user/login/refresh";
        if (!(servletRequest.getRequestURI().equals(refreshUri))) chain.doFilter(request, response);
        else {
            String token = servletRequest.getHeader(jwtTemplate.getHeaderString());
            if (token == null) servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            else if (jwtUtil.isTokenExpired(token)) servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            else if (!jwtUtil.getTypeFromRefreshToken(token).equals(jwtTemplate.getRefreshType())) servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            else {
                UserDetailsInterface userDetails = service.loadUserByUsername(jwtUtil.getUsernameFromRefreshToken(token));

                String jwtToken = jwtUtil.createToken(userDetails);
                String refreshToken = jwtUtil.createRefreshToken(userDetails);
                servletResponse.setStatus(HttpServletResponse.SC_OK);
                servletResponse.addHeader(jwtTemplate.getHeaderString(), jwtTemplate.getTokenPrefix() + jwtToken);
                servletResponse.addHeader(jwtTemplate.getRefreshHeaderString(), jwtTemplate.getTokenPrefix() + refreshToken);

                chain.doFilter(request, response);
            }
        }
    }
}
