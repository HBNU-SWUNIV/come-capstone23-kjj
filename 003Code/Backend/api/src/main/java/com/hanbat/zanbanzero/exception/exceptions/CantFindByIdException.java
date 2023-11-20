package com.hanbat.zanbanzero.exception.exceptions;

import lombok.Getter;

@Getter
public class CantFindByIdException extends Exception{

    public CantFindByIdException(Long id) {
        super("userId : " + id);
    }

    public CantFindByIdException(String key, Long id) {
        super(key + id);
    }

    public CantFindByIdException(String message, Exception e) {
        super(message, e);
    }
}
