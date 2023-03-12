package com.hanbat.zanbanzero.auth.login.filter.util;

import com.hanbat.zanbanzero.entity.user.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;

public class LoginToUser implements LoginFilterInterface {
    @Override
    public UsernamePasswordAuthenticationToken createToken(HttpServletRequest request) {
        User user;
        try {
            user = objectMapper.readValue(request.getInputStream(), User.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
    }
}
