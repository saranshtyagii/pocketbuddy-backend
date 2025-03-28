package com.web.pocketbuddy.entity.utility;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class GenerateUtils {

    public static String generateOtp() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(1000, 10000));
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public static String maskEmailAddress(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }

        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];

        if (username.length() <= 2) {
            return email;
        }

        String maskedUsername = username.charAt(0) + "*".repeat(username.length() - 2) + username.charAt(username.length() - 1);

        return maskedUsername + "@" + domain;
    }


    public static String convetTemplateToString(String fileName) {
        try {
            // Load the HTML template file from the `static` folder
            ClassPathResource resource = new ClassPathResource("static/"+fileName);
            String content = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            return content;
        } catch (IOException e) {
            throw new RuntimeException("Error reading email template", e);
        }
    }


}
