package com.web.pocketbuddy.entity.tracking;

import lombok.Getter;
import org.thymeleaf.util.StringUtils;

@Getter
public class ThreadContextUtils {

    private ThreadContextUtils() {}

    private static boolean loginIn;
    private static int appVersion;
    private static String userId;
    private static String source;
    private static boolean ios;
    private static String ip;
    private static String location;

    public static void buildThread(boolean isLogIn, int appVersion, String userId, String source, String ip, String location) {
        ThreadContextUtils.loginIn = isLogIn;
        ThreadContextUtils.appVersion = appVersion;
        ThreadContextUtils.userId = userId;
        ThreadContextUtils.source = source;
        ThreadContextUtils.ios = StringUtils.equals(source, "pocketbuddy-ios");
        ThreadContextUtils.ip = ip;
        ThreadContextUtils.location = location;
    }

}
