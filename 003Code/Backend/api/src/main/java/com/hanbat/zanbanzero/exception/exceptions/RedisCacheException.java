package com.hanbat.zanbanzero.exception.exceptions;

import lombok.Getter;
import org.springframework.data.redis.serializer.SerializationException;

@Getter
public class RedisCacheException extends SerializationException {

    public RedisCacheException(String m) {
        super(m);
    }
    public RedisCacheException(String m, Exception e) {
        super(m, e);
    }
}
