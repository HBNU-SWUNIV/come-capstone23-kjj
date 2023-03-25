package com.hanbat.zanbanzero.exception.controller.exceptions;

import lombok.Getter;

@Getter
public class RequestDataisNull extends RuntimeException{
    public RequestDataisNull() {
    }

    public RequestDataisNull(String message) {
        super(message);
    }
}
