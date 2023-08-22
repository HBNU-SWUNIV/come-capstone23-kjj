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

    private UserService service;
    private String refreshUri = "/api/user/login/refresh";

    public JwtRefreshFilter(UserService service) {
        this.service = service;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        if (!(servletRequest.getRequestURI().equals(refreshUri))) chain.doFilter(request, response);
        else {
            String token = servletRequest.getHeader(JwtTemplate.HEADER_STRING);
            if (token == null) servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            else if (JwtUtil.isTokenExpired(token)) servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            else if (!JwtUtil.getTypeFromRefreshToken(token).equals(JwtTemplate.REFRESH_TYPE)) servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            else {
                UserDetailsInterface userDetails = service.loadUserByUsername(JwtUtil.getUsernameFromRefreshToken(token));

                String jwtToken = JwtUtil.createToken(userDetails);
                String refreshToken = JwtUtil.createRefreshToken(userDetails);
                servletResponse.setStatus(HttpServletResponse.SC_OK);
                servletResponse.addHeader(JwtTemplate.HEADER_STRING, JwtTemplate.TOKEN_PREFIX + jwtToken);
                servletResponse.addHeader(JwtTemplate.REFRESH_HEADER_STRING, JwtTemplate.TOKEN_PREFIX + refreshToken);

                chain.doFilter(request, response);
            }
        }
    }
}
