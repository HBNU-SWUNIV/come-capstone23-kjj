package com.hanbat.zanbanzero.exception.filter;

import com.hanbat.zanbanzero.exception.filter.SetFilterException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ExceptionHandlerBeforeUsernamePassword extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }
        catch (NullPointerException e) {
            SetFilterException.setResponse(request, response, HttpStatus.INTERNAL_SERVER_ERROR,"인증에 실패하였습니다.");
        }
        catch (AuthenticationServiceException e) {
            SetFilterException.setResponse(request, response, HttpStatus.UNAUTHORIZED,"인증에 실패하였습니다.");
        }
    }
}
