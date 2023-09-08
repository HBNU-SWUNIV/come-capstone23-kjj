package com.hanbat.zanbanzero.exception.exceptions;

import lombok.Getter;

@Getter
public class CreateTokenException extends RuntimeException{
    public CreateTokenException() {}

    public CreateTokenException(String m) { super(m); }

}
