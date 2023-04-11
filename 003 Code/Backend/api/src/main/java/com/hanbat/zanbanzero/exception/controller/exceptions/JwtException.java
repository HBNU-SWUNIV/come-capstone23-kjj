package com.hanbat.zanbanzero.exception.controller.exceptions;

import lombok.Getter;

@Getter
public class JwtException extends RuntimeException{
    public JwtException() {
    }

    public JwtException(String message) {
        super(message);
    }
}
