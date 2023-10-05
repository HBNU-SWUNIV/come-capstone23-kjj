package com.hanbat.zanbanzero.exception.exceptions;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class KeycloakWithdrawException extends AuthenticationException {

    public KeycloakWithdrawException(String message) {
        super(message);
    }
}
