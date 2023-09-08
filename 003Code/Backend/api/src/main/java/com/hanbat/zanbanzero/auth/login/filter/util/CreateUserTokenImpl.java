package com.hanbat.zanbanzero.auth.login.filter.util;

import com.hanbat.zanbanzero.dto.user.LoginDto;
import com.hanbat.zanbanzero.exception.exceptions.CreateTokenException;
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
            throw new CreateTokenException(e.getMessage());
        }
        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
    }
}
