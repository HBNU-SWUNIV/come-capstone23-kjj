package com.hanbat.zanbanzero.auth.login.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.auth.login.filter.util.CustomUriMapper;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.auth.login.filter.util.CreateTokenInterface;
import com.hanbat.zanbanzero.dto.user.user.UserDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class LoginFilter implements Filter {
    private AuthenticationManager authenticationManager;
    private CustomUriMapper customUriMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (((HttpServletRequest) request).getRequestURI().startsWith("/api/login")) {
            try {
                customUriMapper = new CustomUriMapper(request);
            } catch (WrongParameter e) {
                throw new RuntimeException(e);
            }
            attemptAuthentication((HttpServletRequest) request, (HttpServletResponse) response);
            chain.doFilter(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    public LoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    public void attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
        CreateTokenInterface createTokenInterface = customUriMapper.getLoginFilter();

        UsernamePasswordAuthenticationToken token = createTokenInterface.createToken(request);
        token.setDetails(request.getRequestURI());

        Authentication authentication = authenticationManager.authenticate(token);

        successfulAuthentication(response, authentication);
    }

    public void successfulAuthentication(HttpServletResponse response, Authentication authResult){
        UserDetailsInterface principalDetails = (UserDetailsInterface) authResult.getPrincipal();

        // HMAC256
        String JwtToken = JwtUtil.createToken(principalDetails);
        String RefreshToken = JwtUtil.createRefreshToken(principalDetails);

        response.addHeader(JwtTemplate.HEADER_STRING, JwtTemplate.TOKEN_PREFIX + JwtToken);
        response.addHeader(JwtTemplate.REFRESH_HEADER_STRING, JwtTemplate.TOKEN_PREFIX + RefreshToken);
    }
}