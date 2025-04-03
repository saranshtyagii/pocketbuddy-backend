package com.web.pocketbuddy.entity.helper;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class GenerateUtils {

    public static String generateOtp() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(1000, 10000));
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

}
