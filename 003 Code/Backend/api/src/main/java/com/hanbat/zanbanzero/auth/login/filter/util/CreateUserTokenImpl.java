package com.hanbat.zanbanzero.auth.login.filter.util;

import com.hanbat.zanbanzero.dto.user.LoginDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;

public class CreateUserTokenImpl implements CreateTokenInterface {
    @Override
    public UsernamePasswordAuthenticationToken createToken(HttpServletRequest request) {
        LoginDto user;
        try {
            user = objectMapper.readValue(request.getInputStream(), LoginDto.class);
            request.setAttribute("username", user.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
    }
}
