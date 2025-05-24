package com.web.pocketbuddy.utils;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class RedisServices {

    private final RedisTemplate<String, Object> redisTemplate;

    // Set without expiration
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // Set with expiration in seconds
    public void set(String key, Object value, Long expireTimeInSeconds) {
       try {
           redisTemplate.opsForValue().set(key, value, expireTimeInSeconds);
       } catch (Exception e) {
           // ignore
       }
    }

    public String get(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? value.toString() : null;
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
