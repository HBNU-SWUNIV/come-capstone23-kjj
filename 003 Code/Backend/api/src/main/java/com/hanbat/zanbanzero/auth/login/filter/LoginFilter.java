package com.hanbat.zanbanzero.auth.login.filter;

import com.hanbat.zanbanzero.auth.login.filter.util.CustomUriMapper;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.auth.login.filter.util.CreateTokenInterface;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import com.hanbat.zanbanzero.exception.filter.SetFilterException;
import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

public class LoginFilter extends CustomUsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private CustomUriMapper customUriMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (((HttpServletRequest) request).getRequestURI().startsWith("/login")) {
            try {
                customUriMapper = new CustomUriMapper(request);
            } catch (WrongParameter e) {
                throw new RuntimeException(e);
            }
            super.doFilter(request, response, chain);
        } else {
            chain.doFilter(request, response);
        }
    }

    public LoginFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);

        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        CreateTokenInterface createTokenInterface = customUriMapper.getLoginFilter();

        UsernamePasswordAuthenticationToken token = createTokenInterface.createToken(request);
        token.setDetails(request.getRequestURI());

        Authentication authentication = authenticationManager.authenticate(token);
        // Spring의 권한 관리를 위해 return을 통해 세션에 저장
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        UserDetailsInterface principalDetails = (UserDetailsInterface) authResult.getPrincipal();

        // HMAC256
        String JwtToken = JwtUtil.createToken(principalDetails);
        String RefreshToken = JwtUtil.createRefreshToken(principalDetails);

        response.addHeader(JwtTemplate.HEADER_STRING, JwtTemplate.TOKEN_PREFIX + JwtToken);
        response.addHeader(JwtTemplate.REFRESH_HEADER_STRING, JwtTemplate.TOKEN_PREFIX + RefreshToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        SetFilterException.setResponse(request, response, HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다.");;
    }
}