package com.hanbat.zanbanzero.exception.exceptions;

import lombok.Getter;

@Getter
public class SameNameException extends Exception{
    public SameNameException(String m) {
        super(m);
    }
}
