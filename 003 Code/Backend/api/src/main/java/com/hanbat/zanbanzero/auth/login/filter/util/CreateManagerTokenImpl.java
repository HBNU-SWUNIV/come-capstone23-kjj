package com.hanbat.zanbanzero.auth.login.filter.util;

import com.hanbat.zanbanzero.entity.user.manager.Manager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;

public class CreateManagerTokenImpl implements CreateTokenInterface {
    @Override
    public UsernamePasswordAuthenticationToken createToken(HttpServletRequest request) {
        Manager manager;
        try {
            manager = objectMapper.readValue(request.getInputStream(), Manager.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new UsernamePasswordAuthenticationToken(manager.getUsername(), manager.getPassword());
    }
}
