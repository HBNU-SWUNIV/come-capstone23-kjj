package com.hanbat.zanbanzero.auth.login.filter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


public interface LoginFilterInterface {
    ObjectMapper objectMapper = new ObjectMapper();
    UsernamePasswordAuthenticationToken createToken(HttpServletRequest request);
}