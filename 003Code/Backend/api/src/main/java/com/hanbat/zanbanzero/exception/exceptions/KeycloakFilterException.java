package com.hanbat.zanbanzero.exception.exceptions;

import java.io.IOException;

public class KeycloakFilterException extends IOException {

    public KeycloakFilterException(String message, Exception e) {
        super(message, e);
    }
}
