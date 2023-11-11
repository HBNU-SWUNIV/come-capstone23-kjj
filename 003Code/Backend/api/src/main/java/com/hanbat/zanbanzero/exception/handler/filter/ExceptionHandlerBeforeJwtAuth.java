package com.hanbat.zanbanzero.exception.handler.filter;

import com.hanbat.zanbanzero.exception.exceptions.JwtTokenException;
import com.hanbat.zanbanzero.exception.handler.filter.template.SetFilterErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ExceptionHandlerBeforeJwtAuth extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }
        catch (ServletException e) {
            SetFilterErrorResponse.setResponse(request, response, HttpStatus.FORBIDDEN,e.getMessage());
        }
        catch (JwtTokenException e) {
            SetFilterErrorResponse.setResponse(request, response, HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
