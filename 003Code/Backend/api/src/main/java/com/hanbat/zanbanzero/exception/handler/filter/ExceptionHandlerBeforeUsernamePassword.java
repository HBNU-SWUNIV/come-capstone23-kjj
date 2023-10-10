package com.hanbat.zanbanzero.exception.handler.filter;

import com.hanbat.zanbanzero.exception.handler.filter.custom.SetFilterErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ExceptionHandlerBeforeUsernamePassword extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }
        catch (NullPointerException | AuthenticationServiceException | UsernameNotFoundException e) {
            SetFilterErrorResponse.setResponse(request, response, HttpStatus.UNAUTHORIZED,"인증에 실패하였습니다.");
        }
    }
}
