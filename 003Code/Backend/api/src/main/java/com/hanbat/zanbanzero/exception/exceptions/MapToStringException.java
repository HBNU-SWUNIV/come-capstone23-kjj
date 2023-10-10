package com.hanbat.zanbanzero.exception.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;

public class MapToStringException extends JsonProcessingException {
    public MapToStringException(String m, Exception e) {
        super(m, e);
    };
}
