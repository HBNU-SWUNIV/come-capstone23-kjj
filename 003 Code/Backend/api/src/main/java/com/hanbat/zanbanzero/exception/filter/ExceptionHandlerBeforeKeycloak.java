package com.hanbat.zanbanzero.exception.filter;

import com.hanbat.zanbanzero.exception.exceptions.SetFilterErrorResponseException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ExceptionHandlerBeforeKeycloak extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }
        catch (HttpClientErrorException e) {
            try {
                SetFilterErrorResponse.setResponse(request, response, HttpStatus.UNAUTHORIZED,"Keycloak 인증에 실패하였습니다.");
            } catch (SetFilterErrorResponseException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
