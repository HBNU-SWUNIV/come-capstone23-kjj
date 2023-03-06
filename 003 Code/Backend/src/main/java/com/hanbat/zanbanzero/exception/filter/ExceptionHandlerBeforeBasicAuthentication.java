package com.hanbat.zanbanzero.exception.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.hanbat.zanbanzero.exception.filter.SetFilterException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ExceptionHandlerBeforeBasicAuthentication extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException {
        try {
            filterChain.doFilter(request, response);
        }
        catch (IOException e) {
            SetFilterException.setResponse(request, response, HttpStatus.INTERNAL_SERVER_ERROR, "토큰이 없거나 잘못되었습니다.");
        }
        catch (TokenExpiredException e) {
            SetFilterException.setResponse(request, response, HttpStatus.INTERNAL_SERVER_ERROR, "만료된 토큰입니다.");
        }
    }
}
