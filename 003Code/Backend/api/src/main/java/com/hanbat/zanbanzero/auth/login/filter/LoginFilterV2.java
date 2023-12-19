package com.hanbat.zanbanzero.auth.login.filter;

import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import com.hanbat.zanbanzero.auth.util.JwtUtil;
import com.hanbat.zanbanzero.auth.login.filter.util.CreateTokenInterface;
import com.hanbat.zanbanzero.auth.login.user_details.UserDetailsInterface;
import com.hanbat.zanbanzero.auth.util.RedisAuthUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

public class LoginFilterV2 extends AbstractAuthenticationProcessingFilter {
    private final AuthenticationManager authenticationManager;
    private final CreateTokenInterface tokenInterface;
    private final JwtUtil jwtUtil;
    private final JwtTemplate jwtTemplate;
    private final RedisAuthUtil redisAuthUtil;

    public LoginFilterV2(String loginEndPath, AuthenticationManager authenticationManager, CreateTokenInterface tokenInterface, JwtUtil jwtUtil, JwtTemplate jwtTemplate, RedisAuthUtil redisAuthUtil) {
        super(loginEndPath);
        this.authenticationManager = authenticationManager;
        this.tokenInterface = tokenInterface;
        this.jwtUtil = jwtUtil;
        this.jwtTemplate = jwtTemplate;
        this.redisAuthUtil = redisAuthUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = tokenInterface.createToken(request);
        token.setDetails(request.getRequestURI());

        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserDetailsInterface principalDetails = (UserDetailsInterface) authResult.getPrincipal();

        String accessToken = jwtUtil.createToken(principalDetails);
        String refreshToken = jwtUtil.createRefreshToken(principalDetails);

        jwtUtil.setTokenToResponseHeader(response, accessToken, refreshToken);
        redisAuthUtil.setRefreshTokenDataToRedis(principalDetails.getUsername(), refreshToken);

        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        throw new ServletException("login failed");
    }
}