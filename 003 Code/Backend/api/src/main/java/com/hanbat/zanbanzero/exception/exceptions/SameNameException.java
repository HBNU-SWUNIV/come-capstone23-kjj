package com.hanbat.zanbanzero.exception.exceptions;

import lombok.Getter;

@Getter
public class SameNameException extends Exception{
    public SameNameException() {
    }

    public SameNameException(String message) {
        super(message);
    }
}
