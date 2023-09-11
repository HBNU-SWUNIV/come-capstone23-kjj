package com.hanbat.zanbanzero.exception.exceptions;

import lombok.Getter;

@Getter
public class JwtTokenException extends RuntimeException{


    public JwtTokenException(String message) { super(message); }

}
