package com.hanbat.zanbanzero.exception.exceptions;

import lombok.Getter;

@Getter
public class JwtException extends Exception{
    public JwtException(String message, Exception e) {
        super(message, e);
    }
}
