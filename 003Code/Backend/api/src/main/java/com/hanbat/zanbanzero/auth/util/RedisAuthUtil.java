package com.hanbat.zanbanzero.auth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
@RequiredArgsConstructor
public class RedisAuthUtil {
    private final Jedis jedis;

    public void setRefreshTokenDataToRedis(String username, String refreshToken) {
        jedis.set(username, refreshToken);
    }

    public boolean matchRefreshToken(String username, String refreshToken) {
        if (!jedis.exists(username)) return false;
        return refreshToken.equals(jedis.get(username));
    }
}
