package com.hanbat.zanbanzero.exception.exceptions;

import lombok.Getter;

@Getter
public class CantFindByUsernameException extends Exception{

    public CantFindByUsernameException(Long username) {
        super("username : " + username);
    }

    public CantFindByUsernameException(String message, String username) {
        super(message + username);
    }

    public CantFindByUsernameException(String message, Exception e) {
        super(message, e);
    }
}
