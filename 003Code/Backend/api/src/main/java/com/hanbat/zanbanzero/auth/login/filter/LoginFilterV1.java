package com.hanbat.zanbanzero.auth.login.filter;

import com.hanbat.zanbanzero.auth.login.filter.util.CustomUriMapperV1;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.auth.login.filter.util.CreateTokenInterface;
import com.hanbat.zanbanzero.exception.exceptions.LoginFilterException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public class LoginFilterV1 implements Filter {
    private final AuthenticationManager authenticationManager;
    private CustomUriMapperV1 customUriMapperV1;
    private final JwtUtil jwtUtil;
    private final JwtTemplate jwtTemplate;

    private final String loginEndPath;

    public LoginFilterV1(String loginEndPath, AuthenticationManager authenticationManager, JwtUtil jwtUtil, JwtTemplate jwtTemplate) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.jwtTemplate = jwtTemplate;
        this.loginEndPath = loginEndPath;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (((HttpServletRequest) request).getRequestURI().endsWith(loginEndPath)) {
            try {
                customUriMapperV1 = new CustomUriMapperV1(request);
            } catch (WrongParameter e) {
                throw new LoginFilterException(((HttpServletRequest) request).getRequestURI(), e);
            }
            attemptAuthentication((HttpServletRequest) request, (HttpServletResponse) response);
            chain.doFilter(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    public void attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
        CreateTokenInterface createTokenInterface = customUriMapperV1.getLoginFilter();

        UsernamePasswordAuthenticationToken token = createTokenInterface.createToken(request);
        token.setDetails(request.getRequestURI());

        Authentication authentication = authenticationManager.authenticate(token);

        successfulAuthentication(response, authentication);
    }

    public void successfulAuthentication(HttpServletResponse response, Authentication authResult){
        UserDetailsInterface principalDetails = (UserDetailsInterface) authResult.getPrincipal();

        // HMAC256
        String jwtToken = jwtUtil.createToken(principalDetails);
        String refreshToken = jwtUtil.createRefreshToken(principalDetails);

        response.addHeader(jwtTemplate.getHeaderString(), jwtTemplate.getTokenPrefix() + jwtToken);
        response.addHeader(jwtTemplate.getRefreshHeaderString(), jwtTemplate.getTokenPrefix() + refreshToken);
    }
}