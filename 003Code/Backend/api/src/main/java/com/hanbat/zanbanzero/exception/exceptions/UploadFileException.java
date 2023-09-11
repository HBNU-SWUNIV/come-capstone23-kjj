package com.hanbat.zanbanzero.exception.exceptions;

import lombok.Getter;

import java.io.IOException;

@Getter
public class UploadFileException extends IOException{

    public UploadFileException(String message, Exception e) {
        super(message, e);
    }
}
