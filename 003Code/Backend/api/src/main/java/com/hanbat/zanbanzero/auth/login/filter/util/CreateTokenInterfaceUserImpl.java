package com.hanbat.zanbanzero.auth.login.filter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.dto.user.LoginDto;
import com.hanbat.zanbanzero.exception.exceptions.CreateTokenException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;

public class CreateTokenInterfaceUserImpl implements CreateTokenInterface {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public UsernamePasswordAuthenticationToken createToken(HttpServletRequest request) {
        LoginDto user;
        try {
            try (ServletInputStream inputStream = request.getInputStream()) {
                user = objectMapper.readValue(inputStream, LoginDto.class);
                request.setAttribute("username", user.getUsername());
            }
        } catch (IOException e) {
            throw new CreateTokenException(e);
        }
        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
    }
}
