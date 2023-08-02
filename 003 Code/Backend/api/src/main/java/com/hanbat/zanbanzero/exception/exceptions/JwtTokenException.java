package com.hanbat.zanbanzero.exception.exceptions;

import lombok.Getter;

@Getter
public class JwtTokenException extends RuntimeException{
    public JwtTokenException() {}

    public JwtTokenException(String m) { super(m); }

}
