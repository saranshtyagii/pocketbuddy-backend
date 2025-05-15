package com.web.pocketbuddy.utils;

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

    public static String generateGroupDiscoverableId() {
        final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String uuid = UUID.randomUUID().toString().replace("-", "");
        long hash = uuid.hashCode() & 0xFFFFFFFFL; // Convert to unsigned
        StringBuilder shortId = new StringBuilder();
        // Convert hash to base62
        while (shortId.length() < 6) {
            shortId.append(BASE62.charAt((int)(hash % 62)));
            hash /= 62;
        }
        return shortId.reverse().toString();
    }

}
