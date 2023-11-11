package com.hanbat.zanbanzero.config;

import com.hanbat.zanbanzero.exception.tool.SlackTools;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;


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
            slackTools.sendSlackMessage(e, this.getClass().getSimpleName(), "redisCacheManager()", "Redis Configuration failed");
            return new NoOpCacheManager();
        }
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory());
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())) //Serializer 설정
                .entryTtl(Duration.ZERO);
        builder.cacheDefaults(configuration);
        return builder.build();
    }

    /**
     * ConnectionFactory, KeySerializer, ValueSerializer로 RedisTemplate 빈 생성
     * 
     * @return RedisTemplate 빈
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}