package com.hanbat.zanbanzero.service.cache;

import com.hanbat.zanbanzero.service.menu.MenuService;
import com.hanbat.zanbanzero.service.menu.MenuServiceImplV1;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@Service
@RequiredArgsConstructor
public class MenuCacheWarmUpServiceImpl implements CacheWarmUpService{

    private final MenuService menuService;
    private final RedisConnectionFactory redisConnectionFactory;

    private CacheManager getWarmUpCacheManager() {
        RedisCacheManager.RedisCacheManagerBuilder builder = fromConnectionFactory(redisConnectionFactory);
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ZERO);
        builder.cacheDefaults(configuration);
        return builder.build();
    }

    @Override
    @PostConstruct
    public void warmUpCache() {
        String value = MenuServiceImplV1.ALL_FOODS_CACHE_VALUE;
        String key = MenuServiceImplV1.ALL_FOODS_CACHE_KEY;

        CacheManager warmUpCacheManager = getWarmUpCacheManager();
        Cache cache = warmUpCacheManager.getCache(value);
        cache.put(key, menuService.getFood());
    }
}