package com.hanbat.zanbanzero.exception.exceptions;

import lombok.Getter;

import java.io.IOException;

@Getter
public class UploadFileException extends IOException{
    public UploadFileException() {
    }

    public UploadFileException(String message) {
        super(message);
    }
}
