package com.hanbat.zanbanzero.exception.exceptions;

import lombok.Getter;

@Getter
public class WrongRequestDetails extends Exception{
    public WrongRequestDetails() {
    }

    public WrongRequestDetails(String message) {
        super(message);
    }
}
