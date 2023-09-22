package com.hanbat.zanbanzero.exception.exceptions;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class KeycloakJoinException extends AuthenticationException {

    public KeycloakJoinException(String message) {
        super(message);
    }
}
