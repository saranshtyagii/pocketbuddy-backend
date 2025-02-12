package com.web.pocketbuddy.entity.helper;

import java.util.concurrent.ThreadLocalRandom;

public class OtpGenerateUtils {

    public static String generateOtp() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(1000, 10000));
    }

}
