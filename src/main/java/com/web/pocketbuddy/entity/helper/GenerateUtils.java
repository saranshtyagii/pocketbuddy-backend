package com.web.pocketbuddy.entity.helper;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class GenerateUtils {

    private static RedisUtils redisUtils;

    public static void initialize(RedisUtils redisUtil) {
        redisUtils = redisUtil;
    }

    public static String generateOtp(String usernameOrEmail) {
        if (redisUtils == null) {
            throw new IllegalStateException("RedisUtils is not initialized.");
        }
        String previousOtp = redisUtils.get(usernameOrEmail);
        if (StringUtils.isNotBlank(previousOtp)) {
            return previousOtp;
        }
        String newOtp = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 10000));
        redisUtils.set(usernameOrEmail, newOtp, 5L * 60);
        return newOtp;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
