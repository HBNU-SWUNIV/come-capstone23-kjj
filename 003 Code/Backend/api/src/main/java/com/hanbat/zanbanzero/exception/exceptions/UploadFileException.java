package com.hanbat.zanbanzero.exception.exceptions;

import lombok.Getter;

@Getter
public class UploadFileException extends Exception{
    public UploadFileException() {
    }

    public UploadFileException(String message) {
        super(message);
    }
}
