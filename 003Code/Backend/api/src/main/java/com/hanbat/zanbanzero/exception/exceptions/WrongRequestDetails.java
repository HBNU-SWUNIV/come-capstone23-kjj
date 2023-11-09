package com.hanbat.zanbanzero.exception.exceptions;

import lombok.Getter;

@Getter
public class WrongRequestDetails extends Exception{
    public WrongRequestDetails(String message) {
        super(message);
    }

    public WrongRequestDetails(String key, Long value) {
        super(String.join(" : ", key, Long.toString(value)));
    }
}
