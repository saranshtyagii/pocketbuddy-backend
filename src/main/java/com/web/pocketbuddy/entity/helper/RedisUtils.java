package com.web.pocketbuddy.entity.helper;

import com.web.pocketbuddy.service.response.RedisServices;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RedisUtils {

    private final RedisServices redisServices;

    public void set(String key, String value) {
        if(StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("key or value is empty");
        }
        redisServices.set(key, value);
    }

    public String get(String key) {
        if(StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key is empty");
        }
        Object value = redisServices.get(key);
        return value == null ? null : value.toString();
    }


    public void set(String key, String value, Long expireTimeInSeconds) {
        if(StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("key or value is empty");
        }
        if(expireTimeInSeconds == null) {
             expireTimeInSeconds = (long) (24 * 60 * 60);
        }
        redisServices.set(key, value, expireTimeInSeconds);
    }

    public boolean exists(String key) {
        if(StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key is empty");
        }
        return get(key) != null;
    }

    public void del(String key) {
        if(StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key is empty");
        }
        redisServices.delete(key);
    }

}
