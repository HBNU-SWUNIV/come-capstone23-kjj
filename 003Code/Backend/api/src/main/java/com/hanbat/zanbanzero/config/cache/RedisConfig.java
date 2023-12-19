package com.hanbat.zanbanzero.config.cache;

import com.hanbat.zanbanzero.exception.tool.SlackTools;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.time.Duration;

import static org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;


@Configuration
@EnableCaching
@RequiredArgsConstructor
public class RedisConfig {

    private final SlackTools slackTools;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    /**
     * Redis와의 Connection을 만들기 위한 ConnectionFactory 빈
     * 
     * @return RedisConnectionFactory 빈
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    /**
     * cache 정책 설정
     * 
     * @return CacheManager 빈
     */
    @Bean
    public CacheManager redisCacheManager() {
        try {
            redisConnectionFactory().getConnection().close();
        } catch (RedisConnectionFailureException e) {
            slackTools.sendSlackMessage(e, "Redis Configuration failed");
            return new NoOpCacheManager();
        }
        RedisCacheManager.RedisCacheManagerBuilder builder = fromConnectionFactory(redisConnectionFactory());
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ZERO);
        builder.cacheDefaults(configuration);
        return builder.build();
    }

    @Bean
    public Jedis jedis() {
        return new Jedis(host, port);
    }
}