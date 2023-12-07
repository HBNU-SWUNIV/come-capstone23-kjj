package com.hanbat.zanbanzero.exception.exceptions;

public class WrongParameter extends  Exception{

    public WrongParameter(String message) {
        super(message);
    }

    public WrongParameter(String message, String param) {
        super(message + param);
    }
}
