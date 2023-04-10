package com.hanbat.zanbanzero.exception.controller.exceptions;

import lombok.Getter;

@Getter
public class CantFindByIdException extends RuntimeException{
    public CantFindByIdException() {
    }

    public CantFindByIdException(String message) {
        super(message);
    }
}
