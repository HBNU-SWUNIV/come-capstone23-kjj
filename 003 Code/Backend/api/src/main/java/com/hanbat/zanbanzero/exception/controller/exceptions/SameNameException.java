package com.hanbat.zanbanzero.exception.controller.exceptions;

import lombok.Getter;

@Getter
public class SameNameException extends RuntimeException{
    public SameNameException() {
    }

    public SameNameException(String message) {
        super(message);
    }
}
