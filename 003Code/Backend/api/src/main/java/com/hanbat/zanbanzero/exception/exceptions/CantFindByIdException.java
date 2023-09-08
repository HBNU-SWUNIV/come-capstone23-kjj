package com.hanbat.zanbanzero.exception.exceptions;

import lombok.Getter;

@Getter
public class CantFindByIdException extends Exception{
    public CantFindByIdException() {
    }

    public CantFindByIdException(String message) {
        super(message);
    }
}
