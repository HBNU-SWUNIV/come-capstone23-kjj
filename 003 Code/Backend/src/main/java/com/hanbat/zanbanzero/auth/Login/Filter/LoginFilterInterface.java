package com.hanbat.zanbanzero.auth.Login.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.entity.user.manager.Manager;
import com.hanbat.zanbanzero.entity.user.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;

interface LoginFilterInterface {
    ObjectMapper objectMapper = new ObjectMapper();
    UsernamePasswordAuthenticationToken createToken(HttpServletRequest request);
}

class LoginToUser implements LoginFilterInterface {
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

class LoginToManager implements LoginFilterInterface {
    @Override
    public UsernamePasswordAuthenticationToken createToken(HttpServletRequest request){
        Manager manager;
        try {
            manager = objectMapper.readValue(request.getInputStream(), Manager.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new UsernamePasswordAuthenticationToken(manager.getUsername(), manager.getPassword());
    }
}