package com.hanbat.zanbanzero.exception.exceptions;

import lombok.Getter;

@Getter
public class CantFindByIdException extends Exception{
    public CantFindByIdException(String message, Exception e) {
        super(message, e);
    }
}
