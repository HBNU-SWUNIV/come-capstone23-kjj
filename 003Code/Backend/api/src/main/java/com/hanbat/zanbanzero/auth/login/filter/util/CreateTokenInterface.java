package com.hanbat.zanbanzero.auth.login.filter.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


public interface CreateTokenInterface {
    UsernamePasswordAuthenticationToken createToken(HttpServletRequest request);
}