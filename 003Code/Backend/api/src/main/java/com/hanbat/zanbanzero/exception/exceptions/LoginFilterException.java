package com.hanbat.zanbanzero.exception.exceptions;

import java.io.IOException;

public class LoginFilterException extends IOException {

    public LoginFilterException(String m, Exception message) {
        super(m, message);
    }
}
